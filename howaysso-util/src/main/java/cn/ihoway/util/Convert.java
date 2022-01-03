package cn.ihoway.util;

import cn.ihoway.common.CommonSeria;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Convert {

    private static final HowayLog logger = new HowayLog(Convert.class);

    /**
     * input转换为对应bean
     * @param entityClass
     * @param inputClass
     * @param o
     * @return
     */
    public Object inputToBean(String entityClass,String inputClass,Object o){
        logger.info("### covert begin: entityClass:"+entityClass+" inputClass:"+inputClass+" objectClass:"+o.getClass().getName());
        Class<?> inputClz = null;
        Class<?> entityClz = null;
        Object obj = null;
        try {
            inputClz = Class.forName(inputClass);
            entityClz = Class.forName(entityClass);
            obj = entityClz.getDeclaredConstructor().newInstance();
            Field[] objFields = entityClz.getDeclaredFields();
            for(Field f:objFields){
                f.setAccessible(true);
                Field fd = null;

                try {
                    fd = inputClz.getField(f.getName());
                } catch (NoSuchFieldException e) {
                    logger.error(e.getMessage());
                }
                if(fd!=null){
                    f.set(obj,fd.get(o));
                }
            }

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        logger.info("### covert end");
        return  obj;
    }

    public Object hashMapToInput(HashMap<String,Object> map,String inputClass){
        logger.info("hashMapToInput begin:"+ JSON.toJSONString(map)+" inputClass:"+inputClass);
        Class<?> inputClz;
        Object obj = null;
        try {
            inputClz = Class.forName(inputClass);
            obj = inputClz.getDeclaredConstructor().newInstance();
            Field[] objFields = inputClz.getFields();
            for(Field f:objFields){
                f.setAccessible(true);
                Field fd = null;
                try {
                    fd = inputClz.getField(f.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if(fd != null){
                    //该属性是内部类
                    if(fd.getType().getSuperclass()!=null){ //非基本类型才进行赋值
                        if(CommonSeria.class.getName().equals(fd.getType().getSuperclass().getName())){
                            Object tempObj = hashMapToInput((HashMap<String, Object>) map.get(fd.getName()),fd.getType().getName());
                            f.set(obj,tempObj);
                        }else{
                            //对于不继承cn.ihoway.common.CommonSeria的内部类不进行转换
                            if(map.get(fd.getName()) == null || !map.get(fd.getName()).toString().contains("{") || !map.get(fd.getName()).toString().contains("}")){
                                f.set(obj,map.get(fd.getName()));
                            }
                        }
                    }
                    //基本类型自动赋值
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        return obj;
    }


}

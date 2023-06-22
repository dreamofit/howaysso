package cn.ihoway.util;

import cn.ihoway.common.CommonSeria;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 将输入类与bean对象或者map互相转换
 */
public class Convert {

    private static final HowayLog logger = new HowayLog(Convert.class);

    /**
     * input转换为对应bean
     * @param entityClass 被转换的实体类类名
     * @param inputClass 需要被转换的输入类类名
     * @param o 需要被转换的对象
     * @return 结果
     */
    public Object inputToBean(String entityClass,String inputClass,Object o){
        logger.info("### covert begin: entityClass:"+entityClass+" inputClass:"+inputClass+" objectClass:"+o.getClass().getName());
        Class<?> inputClz = null;
        Class<?> entityClz = null;
        Object obj = null;
        try {
            inputClz = Class.forName(inputClass); //获取对应输入类
            entityClz = Class.forName(entityClass); //获取对应bean类
            obj = entityClz.getDeclaredConstructor().newInstance(); //结果对象初始化
            Field[] objFields = entityClz.getDeclaredFields(); //获取bean对象自己的全部属性字段
            for(Field f:objFields){
                f.setAccessible(true);
                Field fd = null;

                try {
                    fd = inputClz.getField(f.getName()); //根据bean对象的字段名称，获取被转换的input类的具体值
                } catch (NoSuchFieldException e) {
                    logger.error(e.getMessage());
                }
                if(fd!=null){
                    f.set(obj,fd.get(o)); //如果该值不为Null，则赋值给bean对象的对应属性
                }
            }

        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        logger.info("### covert end");
        return  obj;
    }

    /**
     * 将map转为对应的input json格式与input需一一对应,包括层级
     * @param json 需被转的map对象
     * @param inputClass 被转的input类类名
     * @return 结果
     */
    public Object jsonToInput(Map<String,Object> json, String inputClass){
        logger.info("hashMapToInput begin:"+ JSON.toJSONString(json)+" inputClass:"+inputClass);
        if(json == null){
            return null;
        }
        Class<?> inputClz;
        Object obj = null;
        try {
            inputClz = Class.forName(inputClass);
            obj = inputClz.getDeclaredConstructor().newInstance();
            Field[] objFields = inputClz.getFields();
            for(Field f : objFields){
                f.setAccessible(true);
                Field fd = null;
                try {
                    fd = inputClz.getField(f.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if(fd != null){
                    //该属性是内部类
                    if(fd.getType().getSuperclass() != null){ //非基本类型才进行赋值
                        if(CommonSeria.class.getName().equals(fd.getType().getSuperclass().getName())){
                            Object tempObj = jsonToInput((Map<String, Object>) json.get(fd.getName()),fd.getType().getName());
                            f.set(obj,tempObj);
                        }else{
                            //对于不继承cn.ihoway.common.CommonSeria的内部类不进行转换
                            if(json.get(fd.getName()) == null || !json.get(fd.getName()).toString().contains("{") || !json.get(fd.getName()).toString().contains("}")){
                                f.set(obj,json.get(fd.getName()));
                            }
                        }
                    }
                    //基本类型自动赋值
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
        logger.info("result:" + JSON.toJSONString(obj));
        return obj;
    }

    /**
     * 将map转为对应的input,map只有最外层,不与input类型一一对应
     * @param map 需被转的map对象
     * @param inputClass 被转的input类类名
     * @return 结果
     */
    public Object mapToInput(Map<String,Object> map, String inputClass){
        logger.info("mapToInput begin:"+ JSON.toJSONString(map)+" inputClass:"+inputClass);
        Class<?> inputClz;
        Object obj = null;
        try {
            inputClz = Class.forName(inputClass);
            obj = inputClz.getDeclaredConstructor().newInstance();
            Field[] objFields = inputClz.getFields();
            for(Field f : objFields){
                //logger.info(f.getName());
                f.setAccessible(true);
                Field fd = null;
                try {
                    fd = inputClz.getField(f.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if(fd != null){
                    //该属性是内部类
                    if(fd.getType().getSuperclass() != null){ //非基本类型才进行赋值
                        if(CommonSeria.class.getName().equals(fd.getType().getSuperclass().getName())){
                            Object tempObj = mapToInput(map,fd.getType().getName());
                            f.set(obj,tempObj);
                        }else{
                            //对于不继承cn.ihoway.common.CommonSeria的内部类不进行转换
                            if(map.get(fd.getName()) == null || !map.get(fd.getName()).toString().contains("{") || !map.get(fd.getName()).toString().contains("}")){
                                //todo 数字转换
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
        logger.info("result:" + JSON.toJSONString(obj));
        return obj;
    }


}

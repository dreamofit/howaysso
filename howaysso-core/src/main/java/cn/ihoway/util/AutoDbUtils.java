package cn.ihoway.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射自动根据statementid操作数据库
 */
public class AutoDbUtils {
    private Logger logger = Logger.getLogger(AutoDbUtils.class);
    public static <T> T excute(String calssName,String statementId,Object parameter) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        Class<?> clz = Class.forName(calssName);
        Object obj = sqlSession.getMapper(clz);
        Method[] methods = clz.getMethods();
        T t = null;
        for(Method m : methods){
            if(statementId.equals(m.getName())){
                m.setAccessible(true);
                t = (T) m.invoke(obj,parameter);
                sqlSession.commit();
                break;
            }
        }
        return t;
    }
}

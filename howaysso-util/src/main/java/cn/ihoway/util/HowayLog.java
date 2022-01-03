package cn.ihoway.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 项目中统一日志输出类，示例如test类所示
 */
public class HowayLog {
    private static final Logger logger = Logger.getLogger(HowayLog.class);
    private Class clazz = HowayLog.class;
    public static void initLog() {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream("C:/github/howaysso/howaysso-util/src/main/resources/log4j.properties");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public HowayLog(){
        HowayLog.initLog();
    }
    public HowayLog(Class clazz){
        this.clazz = clazz;
        HowayLog.initLog();
    }
    public static void info(Class clazz,Object message){
        HowayLog.initLog();
        logger.info("["+clazz.getSimpleName()+"] "+message);
    }
    public static void info(Object message,Class clazz){
        HowayLog.initLog();
        logger.info("["+clazz.getSimpleName()+"] "+message);
    }
    public static void error(Class clazz,Object message){
        HowayLog.initLog();
        logger.error("["+clazz.getSimpleName()+"] "+message);
    }
    public static void error(Object message,Class clazz){
        HowayLog.initLog();
        logger.error("["+clazz.getSimpleName()+"] "+message);
    }
    public void info(Object message){
        logger.info("["+clazz.getSimpleName()+"] "+message);
    }
    public void error(Object message){
        logger.error("["+clazz.getSimpleName()+"] "+message);
    }

}

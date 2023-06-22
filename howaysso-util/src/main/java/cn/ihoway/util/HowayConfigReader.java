package cn.ihoway.util;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 模板配置
* @author howay
*/
public class HowayConfigReader {

   private static Properties prop = null;

   /**
    * 读取指定配置
    */
    public static String getConfig(String path,String key){
        try(InputStream in = new ClassPathResource(path).getInputStream()) {
            prop = new Properties();
            prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));   // 解决中文读取乱码问题
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String) prop.get(key);
    }

    /**
     * 读取指定配置
     */
    public static Integer getIntConfig(String path,String key){
        return Integer.parseInt(getConfig(path,key));
    }

}
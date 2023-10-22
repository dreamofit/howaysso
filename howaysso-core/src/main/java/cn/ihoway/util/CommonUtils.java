package cn.ihoway.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从环境变量中进行取值
 */
public class CommonUtils {
    public static void getByEnv(Properties properties) {
        for(Object key:properties.keySet()){
            String value = properties.getProperty(key.toString());
            String pattern = "\\$\\{([^}]*)\\}";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            boolean flag = false; // 是否修改
            while (m.find()) {
                flag = true;
                String k = m.group(); //需要被替换的字符串
                String v = m.group(1); //${}里面的内容
                String[] temp = v.split(":");
                String env = "";
                if(temp.length>1){
                    env = System.getenv(temp[0]); //从环境变量获取值
                    if(env == null || "".equals(env) ){
                        env = temp[1];
                    }
                }else {
                    env = System.getenv(v); //从环境变量获取值
                    if(env == null || "".equals(env) ){
                        throw new RuntimeException("No environment variable named "+v+" was found");
                    }
                }
                value = value.replace(k,env);
            }
            if(flag){
                properties.setProperty(key.toString(),value);
            }
        }
    }
}

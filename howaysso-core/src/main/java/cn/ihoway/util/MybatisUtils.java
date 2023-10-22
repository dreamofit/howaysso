package cn.ihoway.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            String resource = "mybatis-config.xml";

            InputStream inputStream = Resources.getResourceAsStream(resource);
            Properties properties = getProperties();
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }
    private static Properties getProperties(){
        String propertiesName = "database.properties";
        Properties properties = new Properties();
        InputStream input = MybatisUtils.class.getClassLoader().getResourceAsStream(propertiesName);
        if (input != null) {
            try {
                properties.load(input);
            } catch (IOException var12) {
                throw new RuntimeException("An error occurred while reading classpath property '" + propertiesName + "', see nested exceptions", var12);
            } finally {
                try {
                    input.close();
                } catch (IOException ignored) {
                }

            }
        }
        //重写config，以便获取环境变量的值
        CommonUtils.getByEnv(properties);
        return properties;
    }


}

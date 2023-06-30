package cn.ihoway;

import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.AccessXmlParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class ServiceInit {
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/provider.xml");
        context.start();
        HowayContainer container = new HowayContainer();
        container.start();
        AccessXmlParser.init();
        SpringApplication.run(ServiceInit.class, args);
    }
}

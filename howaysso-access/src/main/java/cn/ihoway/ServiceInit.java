package cn.ihoway;

import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.AccessXmlParser;
import cn.ihoway.util.HowayLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class ServiceInit {

    public static void main( String[] args ) {
        HowayLog logger = new HowayLog(ServiceInit.class);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/provider.xml");
        context.start();
        HowayContainer container = new HowayContainer();
        container.start();
        AccessXmlParser.init();
        SpringApplication.run(ServiceInit.class, args);
        logger.info("*** howaysso服务已经启动 ***");
    }
}

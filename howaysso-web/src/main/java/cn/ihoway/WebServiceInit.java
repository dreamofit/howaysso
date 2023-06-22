package cn.ihoway;

import cn.ihoway.container.HowayContainer;
import cn.ihoway.util.AccessXmlParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebServiceInit {
    public static void main( String[] args ) {
        HowayContainer container = new HowayContainer();
        container.start();
        AccessXmlParser.init();
        SpringApplication.run(WebServiceInit.class, args);
    }
}

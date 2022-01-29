package cn.ihoway;

import cn.ihoway.container.HowayContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSeviceInit {
    public static void main( String[] args ) {
        HowayContainer container = new HowayContainer();
        container.start();
        SpringApplication.run(WebSeviceInit.class, args);
    }
}

package cn.ihoway.util;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HowayContainer {
    public static ClassPathXmlApplicationContext context;
    public HowayContainer(){}
    public static ClassPathXmlApplicationContext getContext(){
        return context;
    }
    public void start(){
        String[] paths = {"dubbo/provider.xml","dubbo/consumer.xml","META-INF/bean.xml"};
        context = new ClassPathXmlApplicationContext(paths);
        context.refresh();
        context.start();
    }
}

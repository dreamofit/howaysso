package cn.ihoway.util;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HowayContainer {
    public static ClassPathXmlApplicationContext context;
    public HowayContainer(){}
    public static ClassPathXmlApplicationContext getContext(){
        return context;
    }
    public void start(){
        String[] paths = {"dubbo/provider.xml","dubbo/consumer.xml"};
        context = new ClassPathXmlApplicationContext(paths);
        context.refresh();
        context.start();
    }
}

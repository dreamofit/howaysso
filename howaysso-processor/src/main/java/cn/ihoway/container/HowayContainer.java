package cn.ihoway.container;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HowayContainer {
    public static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/consumer.xml");
    public HowayContainer(){}
    public static ClassPathXmlApplicationContext getContext(){
        return context;
    }
    public void start(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/consumer.xml");
        context.refresh();
        context.start();
    }
}

package cn.ihoway.util;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 获取dubbo服务和spring bean
 */
public class HowayContainer {
    private static ClassPathXmlApplicationContext dubboContext; //dubbo服务
    private static ClassPathXmlApplicationContext context;  //普通bean
    public HowayContainer(){}

    public static Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }
    public static Object getDubbo(String name) throws BeansException {
        return dubboContext.getBean(name);
    }

    /**
     * 先获取普通bean，这样dubbo服务获取时不会报空指针异常错误
     */
    public void start(){
        context = new ClassPathXmlApplicationContext("META-INF/bean.xml");
        context.refresh();
        context.start();

        String[] paths = {"dubbo/provider.xml","dubbo/consumer.xml"};
        dubboContext = new ClassPathXmlApplicationContext(paths);
        dubboContext.refresh();
        dubboContext.start();
    }
}

package cn.ihoway;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 初始化dubbo服务
 */
public class DubboServerInit {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/provider.xml");
        //System.out.println(context.getDisplayName() + ": here");
        context.start();
        System.out.println("*** howaysso服务已经启动 ***");
        System.in.read();
    }
}

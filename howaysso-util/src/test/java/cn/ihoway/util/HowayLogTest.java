package cn.ihoway.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HowayLogTest {

    @Test
    void info() {
        HowayLog.info(HowayLogTest.class,"test 1");
        HowayLog.info("test 2",HowayLogTest.class);
        HowayLog.error(HowayLogTest.class,"test 3");
        HowayLog.error("test 4",HowayLogTest.class);
    }
    @Test
    void info2(){
        HowayLog logger = new HowayLog(HowayLogTest.class);
        logger.info("this is a info mess");
        logger.error("error");
    }
}
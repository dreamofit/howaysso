package cn.ihoway.util;

import cn.ihoway.entity.User;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class AutoDbUtilsTest {
    private static Logger logger = Logger.getLogger(AutoDbUtilsTest.class);
    @Test
    void excute() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchAlgorithmException {
        String tableName = "cn.ihoway.dao.UserDao";
        String statementId = "insertSelective";
        //String statementId = "deleteByPrimaryKey";
        User user = new User();
        user.setName("howay");
        user.setTel("18890306193");
        String password = "howay@321";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes(StandardCharsets.UTF_8));
        String pass = new BigInteger(1, md.digest()).toString(16);
        user.setPassword(pass);
        user.setRole(999);
        Integer id = 1;
        //AutoDbUtils.excute(tableName,statementId,id);
        //AutoDbUtils.excute(tableName,statementId,user);
        logger.info("success");
    }
}
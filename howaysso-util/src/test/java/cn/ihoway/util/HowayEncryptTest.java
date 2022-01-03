package cn.ihoway.util;

import cn.ihoway.type.AlgorithmType;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class HowayEncryptTest {

    @Test
    void md5() throws NoSuchAlgorithmException {
        System.out.println(HowayEncrypt.md5("howay@321"));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.SHA1.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.SHA256.getAlgorithm(),4));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.SHA384.getAlgorithm(),8));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.SHA512.getAlgorithm(),8));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.MD2.getAlgorithm(),10));
        System.out.println(HowayEncrypt.encrypt("howay@321", AlgorithmType.MD5.getAlgorithm(),10));
        System.out.println("**************************************************************************");
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.MD5.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.MD2.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.SHA1.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.SHA256.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.SHA384.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt(AlgorithmType.SHA512.getAlgorithm(), AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println("**************************************************************************");
        System.out.println(HowayEncrypt.encrypt("0", AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("1", AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("2", AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("3", AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("4",AlgorithmType.MD5.getAlgorithm(),2));
        System.out.println(HowayEncrypt.encrypt("5", AlgorithmType.MD5.getAlgorithm(),2));
    }
}
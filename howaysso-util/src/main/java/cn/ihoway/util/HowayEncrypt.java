package cn.ihoway.util;

import cn.ihoway.type.AlgorithmType;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * SHA-1 SHA-256 SHA-384 SHA-512 MD5 MD2
 */
public class HowayEncrypt {
    private static final HowayLog logger = new HowayLog(HowayEncrypt.class);

    public static String md5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(AlgorithmType.MD5.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        assert md != null;
        md.update(str.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     *
     * @param str 加密字符串
     * @param algorithm 加密算法
     * @param index 截取位数
     * @return 结果
     */
    public static String encrypt(String str,String algorithm,int index){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        assert md != null;
        md.update(str.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, md.digest()).toString(16).substring(0,index);
    }

}

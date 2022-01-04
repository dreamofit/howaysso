package cn.ihoway.security;

import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.service.UserService;
import cn.ihoway.type.AlgorithmType;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;

import java.util.Random;

/**
 * 网站access token
 * <p>暂定规则：用户登录时生成token,发起请求时携带token并更新时间，时间大于15Min即超时,需要重新登录,否则允许登录。
 * 首先检查token是否存在且有效
 */
public class HowayAccessToken {

    private final HowayLog logger = new HowayLog(HowayAccessToken.class);
    private final UserService service = new UserServiceImpl();

    public static final int HEAD_PLACEHOLDER = 4; //随机数占用位数
    public static final int ID_PLACEHOLDER = 4; //用户id占用位数
    private static final int ALGORITHM_PLACEHOLDER = 2; //算法位占用位数(固定)
    private static final int NAME_PASSWORD_PLACEHOLDER = 8; //用户名密码占用位数
    private static final int APP_KEY_APP_SECRET_PLACEHOLDER = 6; //key和secert占用位数
    private static final int TIMESTAMP_PLACEHOLDER = 9; //时间戳占用位数（固定）
    private static final int SIGN_PLACEHOLDER = 4; // 签名占用位数
    private static final int EXPIRATION_TIME = 15; // 过期时间 (min)

    /**
     * 按规则生成token
     * @param uid uid
     * @param name 用户名
     * @param password 密码
     * @param appKey 网站key
     * @param appSecret 网站密码
     * @return token
     */
    public String getToken(Integer uid,String name,String password,String appKey,String appSecret){
        String token = "";
        Random random = new Random();
        int firstRandom = random.nextInt(10000)+1000;
        int secondRandom = random.nextInt(6);
        int thirdRandom = random.nextInt(6);
        logger.info("生成三个随机数：" + firstRandom + " | " + secondRandom + " | " + thirdRandom);
        //四位随机数
        token = HowayEncrypt.encrypt(String.valueOf(firstRandom), AlgorithmType.MD5.getAlgorithm(), HEAD_PLACEHOLDER);
        token += leftLetter(String.valueOf(uid));
        //两位随机算法
        token += HowayEncrypt.encrypt(String.valueOf(secondRandom),AlgorithmType.MD5.getAlgorithm(),ALGORITHM_PLACEHOLDER);
        //用随机算法加密用户名和密码
        token += HowayEncrypt.encrypt(name+password,AlgorithmType.values()[secondRandom].getAlgorithm(),NAME_PASSWORD_PLACEHOLDER);
        //两位随机算法
        token += HowayEncrypt.encrypt(String.valueOf(thirdRandom),AlgorithmType.MD5.getAlgorithm(),ALGORITHM_PLACEHOLDER);
        //用随机算法加密appkey和appsecret
        token += HowayEncrypt.encrypt(appKey+appSecret,AlgorithmType.values()[thirdRandom].getAlgorithm(),APP_KEY_APP_SECRET_PLACEHOLDER);
        //时间戳
        token += String.valueOf(System.currentTimeMillis()).substring(0,TIMESTAMP_PLACEHOLDER);
        //将上述全部进行md5加密
        token += HowayEncrypt.encrypt(token,AlgorithmType.MD5.getAlgorithm(), SIGN_PLACEHOLDER);

        return token;
    }

    /**
     * 检查token是否符合生成规则，检查是否过期
     * @param token token
     * @param name 用户名
     * @param password 密码
     * @param appKey appKey
     * @param appSecret appSecret
     * @return true or false
     */
    protected boolean isToekenRule(String token,String name,String password,String appKey,String appSecret){
        logger.info("token:"+token);
        int len = HEAD_PLACEHOLDER + ID_PLACEHOLDER + ALGORITHM_PLACEHOLDER*2 + NAME_PASSWORD_PLACEHOLDER + APP_KEY_APP_SECRET_PLACEHOLDER + TIMESTAMP_PLACEHOLDER+SIGN_PLACEHOLDER;
        if(token.length() != len){
            logger.info("token长度异常！");
            return false;
        }
        //规则一：前面22经过md5加密后与后四位一致
        if(!token.substring(len-SIGN_PLACEHOLDER,len).equals(HowayEncrypt.encrypt(token.substring(0,len-SIGN_PLACEHOLDER),AlgorithmType.MD5.getAlgorithm(),SIGN_PLACEHOLDER))){
            logger.info("token签名异常！");
            return false;
        }

        int start = HEAD_PLACEHOLDER + ID_PLACEHOLDER;
        int end = HEAD_PLACEHOLDER + ID_PLACEHOLDER + ALGORITHM_PLACEHOLDER;
        //规则二，获取下标4、5位的第一个随机算法，用该随机算法计算name+password与下标6-13位字符串一致
        int algorithm = getAlgorithm(token.substring(start,end));
        if(algorithm < 0){
            logger.info("token随机算法1异常");
            return false;
        }
        start = end;
        end += NAME_PASSWORD_PLACEHOLDER;
        if(!token.substring(start,end).equals(HowayEncrypt.encrypt(name+password,AlgorithmType.values()[algorithm].getAlgorithm(),NAME_PASSWORD_PLACEHOLDER))){
            logger.info("token用户和密码加密异常");
            return false;
        }
        //规则三，获取14-15位的第二个随机算法，用该随机算法计算appkey+appSecret与第16-21位字符串一致
        start = end;
        end += ALGORITHM_PLACEHOLDER;
        algorithm = getAlgorithm(token.substring(start,end));
        if(algorithm < 0){
            logger.info("token随机算法2异常");
            return false;
        }
        start = end;
        end += APP_KEY_APP_SECRET_PLACEHOLDER;
        if(!token.substring(start, end).equals(HowayEncrypt.encrypt(appKey + appSecret, AlgorithmType.values()[algorithm].getAlgorithm(), APP_KEY_APP_SECRET_PLACEHOLDER))){
            logger.info("token key和secret加密异常");
            return false;
        }
        start = end;
        end += TIMESTAMP_PLACEHOLDER;
        long timestamp = Long.parseLong(token.substring(start,end)+"0000");
        long diff = (System.currentTimeMillis() - timestamp) / 1000 / 60;
        if(diff > EXPIRATION_TIME){
            logger.info("token已超时");
            return false;
        }
        return true;
    }

    public boolean isToekenRule(String token,String appKey,String appSecret){
        User user = getUserByToken(token);
        return isToekenRule(token,user.getName(),user.getPassword(),appKey,appSecret);
    }

    public User getUserByToken(String token){
        String uidStr = moveLeftLetter(token.substring(HEAD_PLACEHOLDER,HEAD_PLACEHOLDER + ID_PLACEHOLDER));
        int uid = Integer.parseInt(uidStr);
        return service.findById(uid);
    }

    private String leftLetter(String str){
        StringBuilder left = new StringBuilder();
        if(str.length() >= ID_PLACEHOLDER){
            return str;
        }
        String letterString = "abcdefghijklmnopqrstuvwxyz";
        char[] letterChar = letterString.toCharArray();
        Random random = new Random();
        for(int i = 0;i < ID_PLACEHOLDER - str.length();i++){
            left.append(letterChar[random.nextInt(letterChar.length)]);
        }
        return left + str;
    }
    private String moveLeftLetter(String str){
        int index = 0;
        for(int i = 0; i < str.length(); i++){
            char ch = str.charAt(i);
            if(ch >= 48 && ch <= 57){
                index = i;
                break;
            }
        }
        return str.substring(index,ID_PLACEHOLDER);
    }

    private int getAlgorithm(String s){
        return switch (s) {
            case "cf" -> 0;
            case "c4" -> 1;
            case "c8" -> 2;
            case "ec" -> 3;
            case "a8" -> 4;
            case "e4" -> 5;
            default -> -1;
        };
    }
}

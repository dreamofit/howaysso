package cn.ihoway.provider.security;

import cn.ihoway.entity.Site;
import cn.ihoway.entity.User;
import cn.ihoway.impl.SiteServiceImpl;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.service.SiteService;
import cn.ihoway.service.UserService;
import cn.ihoway.type.AlgorithmType;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayConfigReader;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;

import java.util.Arrays;
import java.util.Random;

/**
 * 网站access token
 * <p>暂定规则：用户登录时生成token,发起请求时携带token并更新时间，时间大于15Min即超时,需要重新登录,否则允许登录。
 * 首先检查token是否存在且有效
 */
public class HowayAccessToken {

    private final HowayLog logger = new HowayLog(HowayAccessToken.class);
    private final UserService userService = new UserServiceImpl();
    private final SiteService siteService = new SiteServiceImpl();

    public static final int HEAD_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.head"); //随机数占用位数
    public static final int ID_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.id");; //用户id占用位数
    private static final int ALGORITHM_PLACEHOLDER = 2; //算法位占用位数(固定)
    private static final int NAME_PASSWORD_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.password");; //用户名密码占用位数
    private static final int APP_KEY_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.key");; //app_key
    private static final int APP_KEY_APP_SECRET_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.secret");; //key和secret占用位数
    private static final int TIMESTAMP_PLACEHOLDER = 9; //时间戳占用位数（固定）
    private static final int SIGN_PLACEHOLDER = HowayConfigReader.getIntConfig("token.properties","placeHolder.sign");; // 签名占用位数
    private static final int EXPIRATION_TIME = HowayConfigReader.getIntConfig("token.properties","time.expiration");; // 过期时间 (min)

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
        //logger.info("生成三个随机数：" + firstRandom + " | " + secondRandom + " | " + thirdRandom);
        //四位随机数
        token = HowayEncrypt.encrypt(String.valueOf(firstRandom), AlgorithmType.MD5.getAlgorithm(), HEAD_PLACEHOLDER);
        token += leftLetter(String.valueOf(uid));
        //两位随机算法
        token += HowayEncrypt.encrypt(String.valueOf(secondRandom),AlgorithmType.MD5.getAlgorithm(),ALGORITHM_PLACEHOLDER);
        //用随机算法加密用户名和密码
        token += HowayEncrypt.encrypt(name+password,AlgorithmType.values()[secondRandom].getAlgorithm(),NAME_PASSWORD_PLACEHOLDER);
        //app_key
        token += appKey.substring(0,APP_KEY_PLACEHOLDER);
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

    public String getTokenByToken(String token,String appKey,String appSecret){
        try {
            User user = getUserByToken(token);
            return getToken(user.getId(),user.getName(),user.getPassword(),appKey,appSecret);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return "";
        }
    }

    /**
     * 检查token是否符合生成规则，检查是否过期
     * @param token token
     * @param name 用户名
     * @param password 密码
     * @return true or false
     */
    protected StatusCode isToekenRule(String token,String name,String password){
        //logger.info("token:"+token);
        int len = HEAD_PLACEHOLDER + ID_PLACEHOLDER + ALGORITHM_PLACEHOLDER*2 + NAME_PASSWORD_PLACEHOLDER + APP_KEY_PLACEHOLDER + APP_KEY_APP_SECRET_PLACEHOLDER + TIMESTAMP_PLACEHOLDER+SIGN_PLACEHOLDER;
        if(token.length() != len){
            logger.info("token长度异常！");
            return StatusCode.TOKENERROR;
        }
        //logger.info("len:"+len);
        //规则一：前面22经过md5加密后与后四位一致
        if(!token.substring(len-SIGN_PLACEHOLDER,len).equals(HowayEncrypt.encrypt(token.substring(0,len-SIGN_PLACEHOLDER),AlgorithmType.MD5.getAlgorithm(),SIGN_PLACEHOLDER))){
            logger.info("token签名异常！");
            return StatusCode.TOKENERROR;
        }

        int start = HEAD_PLACEHOLDER + ID_PLACEHOLDER;
        int end = HEAD_PLACEHOLDER + ID_PLACEHOLDER + ALGORITHM_PLACEHOLDER;
        //规则二，获取下标4、5位的第一个随机算法，用该随机算法计算name+password与下标6-13位字符串一致
        int algorithm = getAlgorithm(token.substring(start,end));
        if(algorithm < 0){
            logger.info("token随机算法1异常");
            return StatusCode.TOKENERROR;
        }
        start = end;
        end += NAME_PASSWORD_PLACEHOLDER;
        if(!token.substring(start,end).equals(HowayEncrypt.encrypt(name+password,AlgorithmType.values()[algorithm].getAlgorithm(),NAME_PASSWORD_PLACEHOLDER))){
            logger.info("token用户和密码加密异常" + token.substring(start,end) + " " + HowayEncrypt.encrypt(name+password,AlgorithmType.values()[algorithm].getAlgorithm(),NAME_PASSWORD_PLACEHOLDER));
            return StatusCode.TOKENERROR;
        }
        //规则三，获取app_key，查询数据库中是否存在该key,并获取想应的secret
        end += APP_KEY_PLACEHOLDER;
        Site site;
        try {
            site = getSiteByToken(token);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return StatusCode.JAVAEXCEPTION;
        }
        if(site == null){
            logger.info("token appkey失效或者错误！");
            return StatusCode.TOKENERROR;
        }
        String appKey = site.getAppkey();
        String appSecret = site.getAppsecret();
        //logger.info("appkey:"+appKey+" appsecret:"+appSecret);

        //规则四，获取14-15位的第二个随机算法，用该随机算法计算appkey+appSecret与第16-21位字符串一致
        start = end;
        end += ALGORITHM_PLACEHOLDER;
        algorithm = getAlgorithm(token.substring(start,end));
        if(algorithm < 0){
            logger.info("token随机算法2异常");
            return StatusCode.TOKENERROR;
        }
        start = end;
        end += APP_KEY_APP_SECRET_PLACEHOLDER;
        if(!token.substring(start, end).equals(HowayEncrypt.encrypt(appKey + appSecret, AlgorithmType.values()[algorithm].getAlgorithm(), APP_KEY_APP_SECRET_PLACEHOLDER))){
            logger.info("token key和secret加密异常");
            return StatusCode.TOKENERROR;
        }
        start = end;
        end += TIMESTAMP_PLACEHOLDER;
        long timestamp = Long.parseLong(token.substring(start,end)+"0000");
        long diff = (System.currentTimeMillis() - timestamp) / 1000 / 60;
        if(diff > EXPIRATION_TIME){
            logger.info("token已超时");
            return StatusCode.TOKENTIMEOUT;
        }
        return StatusCode.SUCCESS;
    }

    private Site getSiteByToken(String token) throws Exception{
        int start = HEAD_PLACEHOLDER + ID_PLACEHOLDER + ALGORITHM_PLACEHOLDER + NAME_PASSWORD_PLACEHOLDER;
        int end = start + APP_KEY_PLACEHOLDER;
        String appKey = token.substring(start,end);
        return siteService.findSiteByAppKey(appKey);
    }

    public StatusCode isToekenRule(String token, int level) {
        User user;
        try {
            user = getUserByToken(token);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return StatusCode.JAVAEXCEPTION;
        }
        if(user == null){
            logger.info("用户不存在");
            return StatusCode.TOKENERROR;
        }
        if(user.getRole() < level){
            logger.info("用户"+user.getName()+"权限不足");
            logger.info(user.getName()+"等级:"+user.getRole()+" 最低权限要求:"+level);
            return StatusCode.PERMISSIONDENIED;
        }
        return isToekenRule(token,user.getName(),user.getPassword());
    }

    public User getUserByToken(String token) throws Exception{
        String uidStr = moveLeftLetter(token.substring(HEAD_PLACEHOLDER,HEAD_PLACEHOLDER + ID_PLACEHOLDER));
        int uid = Integer.parseInt(uidStr);
        return userService.findById(uid);
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
    private String moveLeftLetter(String str) throws Exception{
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

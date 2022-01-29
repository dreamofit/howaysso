package cn.ihoway.api.security;

import java.util.HashMap;

public interface TokenAsm {
    //1.getToken
    String getToken(String token,String appkey,String appSecret);
    //2.是否符合token规则
    HashMap<String,Object> isToekenRule(String token,int level);
    //3.从token中获取用户信息
    HashMap<String,Object> getUserByToken(String token);
}

package cn.ihoway.security;

import cn.ihoway.api.security.TokenAsm;
import cn.ihoway.type.StatusCode;

import java.util.HashMap;

public class TokenAsmIml implements TokenAsm {
    private HowayAccessToken accessToken = new HowayAccessToken();
    @Override
    public String getToken(String token, String appkey, String appSecret) {
        return accessToken.getTokenByToken(token,appkey,appSecret);
    }

    @Override
    public HashMap<String, Object> isToekenRule(String token,int level) {
        StatusCode res = accessToken.isToekenRule(token,level);
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",res.getCode());
        map.put("msg",res.getMsg());
        return map;
    }

    @Override
    public HashMap<String, Object> getUserByToken(String token) {
        return null;
    }
}

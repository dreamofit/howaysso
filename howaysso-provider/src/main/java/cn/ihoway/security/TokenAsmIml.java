package cn.ihoway.security;

import cn.ihoway.api.security.TokenAsm;
import cn.ihoway.entity.User;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;

public class TokenAsmIml implements TokenAsm {
    private HowayAccessToken accessToken = new HowayAccessToken();
    private final HowayLog logger = new HowayLog(TokenAsmIml.class);

    @Override
    public String getToken(String token, String appkey, String appSecret) {
        return accessToken.getTokenByToken(token,appkey,appSecret);
    }

    @Override
    public HashMap<String, Object> isTokenRule(String token,int level) {
        StatusCode res = accessToken.isToekenRule(token,level);
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",res.getCode());
        map.put("msg",res.getMsg());
        return map;
    }

    @Override
    public HashMap<String, Object> getUserByToken(String token) {
        try {
            User user = accessToken.getUserByToken(token);
            user.setPassword(null);
            return JSON.parseObject(JSON.toJSONString(user), HashMap.class);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}

package cn.ihoway.provider.security;

import cn.ihoway.api.security.TokenAsm;
import cn.ihoway.entity.User;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.HowayLog;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 提供给其他应用与token相关的服务
 */
@Service
public class TokenAsmIml implements TokenAsm {
    private final HowayAccessToken accessToken = (HowayAccessToken) HowayContainer.getBean("token");
    private final HowayLog logger = new HowayLog(TokenAsmIml.class);

    /**
     * 根据旧的token生成新的可用的token
     * @param token token
     * @param appkey appkey
     * @param appSecret appSecret
     * @return newToken
     */
    @Override
    public String getToken(String token, String appkey, String appSecret) {
        String newToken = accessToken.getTokenByToken(token,appkey,appSecret);
        logger.info("token : " + token + " 生成新的token： " + newToken);
        return newToken;
    }

    /**
     * 检查token是否符合规则，权限是否足够
     * @param token token
     * @param level 最低权限要求
     * @return StatusCode
     */
    @Override
    public HashMap<String, Object> isTokenRule(String token,int level) {
        StatusCode res = accessToken.isTokenRule(token,level);
        logger.info("token : " + token + " msg : " + res.getMsg());
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",res.getCode());
        map.put("msg",res.getMsg());
        return map;
    }

    
    /**
     * 根据token获取用户信息
     * @param token token
     * @return 无密码字段的用户信息
     */
    @Override
    public HashMap<String, Object> getUserByToken(String token) {
        try {
            User user = accessToken.getUserByToken(token);
            String password = user.getPassword();
            //不给其他应用返回密码字段
            user.setPassword(null);
            HashMap<String,Object> userMap = JSON.parseObject(JSON.toJSONString(user), HashMap.class);
            user.setPassword(password);
            return userMap;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}

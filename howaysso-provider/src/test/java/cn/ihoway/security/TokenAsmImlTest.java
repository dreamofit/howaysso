package cn.ihoway.security;

import cn.ihoway.entity.User;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TokenAsmImlTest {
    private HowayAccessToken accessToken = new HowayAccessToken();
    @Test
    void getToken() {
    }

    @Test
    void isTokenRule() {
    }

    @Test
    void getUserByToken() throws Exception {
        User user = accessToken.getUserByToken("b85mys4c8bfd2d25582a88027bf164345190fa2f");
        user.setPassword(null);
        HashMap<String, Object> map = JSON.parseObject(JSON.toJSONString(user), HashMap.class);
        System.out.println(map.toString());
    }
}
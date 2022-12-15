package cn.ihoway.user;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


class UserAsmImplTest {

    UserAsmImpl userAsm = new UserAsmImpl();

    @Test
    void login() {
        HashMap<String, Object> inputHashMap = new HashMap<>();
        HashMap<String,Object> inChomm = new HashMap<>();
        inChomm.put("name","张三");
        inChomm.put("password","1234567");
        inputHashMap.put("inChomm",inChomm);
        HashMap<String,Object> out = userAsm.login(inputHashMap);
        System.out.println(JSON.toJSONString(out));
    }
}
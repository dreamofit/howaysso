package cn.ihoway.api.user;

import java.util.HashMap;

public interface UserAsm {
    HashMap<String,Object> login(HashMap<String,Object> input);
    HashMap<String,Object> getUserById(Integer id,String eventNo,String traceId);
}

package cn.ihoway.provider.user;

import cn.ihoway.api.user.UserAsm;
import cn.ihoway.entity.User;
import cn.ihoway.processor.user.io.UserSearchOutput;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;

public class UserAsmImpl implements UserAsm {

    private final HowayLog logger = new HowayLog(UserAsmImpl.class);

    @Override
    public HashMap<String, Object> login(HashMap<String, Object> inputHashMap) {
        HowayResult result = AccessRoute.handle(null,"login",inputHashMap);
        return  JSON.parseObject(result.toString(),HashMap.class);
    }

    @Override
    public HashMap<String, Object> getUserById(Integer id,String eventNo,String traceId) {
        logger.info(traceId+" : id: " + id + " eventNo: "+eventNo);
        HashMap<String,Object> inputHashMap = new HashMap<>();
        inputHashMap.put("uid",id);
        inputHashMap.put("type",UserSearchType.ONLYID);
        inputHashMap.put("eventNo",eventNo);
        inputHashMap.put("traceId",traceId);
        HowayResult result = AccessRoute.handle(null,"userNoTokenSearch",inputHashMap);
        UserSearchOutput output = (UserSearchOutput) result.getData();
        logger.info("output : "+JSON.toJSONString(output));
        List<User> userList = output.userList;
        if(userList != null && userList.size() > 0){
            User user = userList.get(0);
            return JSON.parseObject(JSON.toJSONString(user),HashMap.class);
        }
        return null;
    }


}

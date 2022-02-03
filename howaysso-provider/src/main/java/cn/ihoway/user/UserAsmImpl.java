package cn.ihoway.user;

import cn.ihoway.api.user.UserAsm;
import cn.ihoway.entity.User;
import cn.ihoway.processor.user.UserLoginProcessor;
import cn.ihoway.processor.user.UserNoTokenSearchProcessor;
import cn.ihoway.processor.user.io.UserLoginInput;
import cn.ihoway.processor.user.io.UserLoginOutput;
import cn.ihoway.processor.user.io.UserSearchInput;
import cn.ihoway.processor.user.io.UserSearchOutput;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;

public class UserAsmImpl implements UserAsm {

    private final Convert convert = new Convert();
    private final HowayLog logger = new HowayLog(UserAsmImpl.class);

    @Override
    public HashMap<String, Object> login(HashMap<String, Object> inputHashMap) {
        UserLoginProcessor loginProcessor = new UserLoginProcessor();
        UserLoginInput input = (UserLoginInput) convert.hashMapToInput(inputHashMap,UserLoginInput.class.getName());
        UserLoginOutput output = new UserLoginOutput();
        HowayResult result = loginProcessor.doExecute(input,output);
        return  JSON.parseObject(result.toString(),HashMap.class);
    }

    @Override
    public HashMap<String, Object> getUserById(Integer id,String eventNo,String traceId) {
        logger.info(traceId+" : id: " + id + " eventNo: "+eventNo);
        UserNoTokenSearchProcessor processor = new UserNoTokenSearchProcessor();
        UserSearchInput input = new UserSearchInput();
        UserSearchOutput output = new UserSearchOutput();
        input.inChomm.uid = id;
        input.inChomm.type = UserSearchType.ONLYID;
        input.eventNo = eventNo;
        input.traceId = traceId;
        HowayResult result = processor.doExecute(input,output);
        output = (UserSearchOutput) result.getData();
        logger.info("output tostring: "+JSON.toJSONString(output));
        List<User> userList = output.userList;
        if(userList != null && userList.size() > 0){
            User user = userList.get(0);
            return JSON.parseObject(JSON.toJSONString(user),HashMap.class);
        }
        return null;
    }


}

package cn.ihoway.user;

import cn.ihoway.api.user.UserAsm;
import cn.ihoway.processor.user.UserLoginProcessor;
import cn.ihoway.processor.user.io.UserLoginInput;
import cn.ihoway.processor.user.io.UserLoginOutput;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;

public class UserAsmImpl implements UserAsm {

    private UserLoginProcessor loginProcessor = new UserLoginProcessor();
    private Convert convert = new Convert();

    @Override
    public HashMap<String, Object> login(HashMap<String, Object> inputHashMap) {
        UserLoginInput input = (UserLoginInput) convert.hashMapToInput(inputHashMap,UserLoginInput.class.getName());
        UserLoginOutput output = new UserLoginOutput();
        HowayResult result = loginProcessor.doExcute(input,output);
        return  JSON.parseObject(result.toString(),HashMap.class);
    }
}

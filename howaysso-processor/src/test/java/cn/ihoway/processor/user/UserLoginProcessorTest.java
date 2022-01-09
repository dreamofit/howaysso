package cn.ihoway.processor.user;

import cn.ihoway.processor.user.io.UserLoginInput;
import cn.ihoway.processor.user.io.UserLoginOutput;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UserLoginProcessorTest {
    private UserLoginProcessor processor = new UserLoginProcessor();

    @Test
    void process() {
        UserLoginInput input = new UserLoginInput();
        input.inChomm.name = "李四";
        input.inChomm.password = "123456";
        UserLoginOutput output = new UserLoginOutput();
        HowayResult result = processor.doExcute(input,output);
        System.out.println(result.toString());
        assert result.isOk();
    }

    @Test
    void test() throws ClassNotFoundException {
        HashMap<String,Object> map = new HashMap<>();
        HashMap<String,Object> inChomm = new HashMap<>();
        HashMap<String,Object> common = new HashMap<>();
        common.put("set","0");
        inChomm.put("name","张三");
        inChomm.put("password","123456");
        map.put("inChomm",inChomm);
        map.put("common",common);
        map.put("set",0);
        UserLoginInput input = new UserLoginInput();

        Convert convert = new Convert();
        input = (UserLoginInput) convert.hashMapToInput(map,UserLoginInput.class.getName());
        System.out.println(JSON.toJSONString(input));
    }

    void getValue(HashMap<String,Object> map,UserLoginInput input,String filed){
        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key = entry.getKey();
            Object object = entry.getValue();
            if(object instanceof HashMap<?,?>){
                System.out.println(key+": yes");
                HashMap<String,Object> tempMap = (HashMap<String, Object>) object;
                getValue(tempMap,input,key);
            }else{
                if("".equals(filed)){
                    //input
                }
            }
        }
    }


}
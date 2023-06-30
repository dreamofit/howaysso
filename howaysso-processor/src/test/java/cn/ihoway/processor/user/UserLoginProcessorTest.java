package cn.ihoway.processor.user;

import cn.ihoway.processor.user.io.UserLoginInput;
import cn.ihoway.processor.user.io.UserLoginOutput;
import cn.ihoway.util.AccessRoute;
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

    }

    @Test
    void test() throws ClassNotFoundException {
        HashMap<String,Object> map = new HashMap<>();
        HashMap<String,Object> inChomm = new HashMap<>();
        inChomm.put("name","张三");
        inChomm.put("password","123456");
        map.put("inChomm",inChomm);
        map.put("token","123");
        UserLoginInput input;

        Convert convert = new Convert();
        input = (UserLoginInput) convert.jsonToInput(map,UserLoginInput.class.getName());
        System.out.println(JSON.toJSONString(input));
    }

    @Test
    void test2() throws ClassNotFoundException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("password","123456");
        map.put("token","123");
        UserLoginInput input;

        Convert convert = new Convert();
        input = (UserLoginInput) convert.mapToInput(map,UserLoginInput.class.getName());
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
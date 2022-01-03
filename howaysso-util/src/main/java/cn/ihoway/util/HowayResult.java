package cn.ihoway.util;


import cn.ihoway.type.StatusCode;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 返回状态码和输出信息以及数据，强制：所有接口统一使用该类进行返回
 */
public class HowayResult implements Serializable {
    private StatusCode statusCode;
    private String condition;
    private Object data;
    HowayResult(){}
    HowayResult(StatusCode statusCode, String condition, Object data){
        this.statusCode = statusCode;
        this.condition = condition;
        this.data = data;
    }
    public static HowayResult createResult(StatusCode statusCode,String condition,Object data){
        return new HowayResult(statusCode,condition,data);
    }
    public static HowayResult createResult(StatusCode statusCode,Object data){
        return createResult(statusCode,"",data);
    }

    public static HowayResult createSuccessResult(String condition,Object data){
        return createResult(StatusCode.SUCCESS,condition,data);
    }
    public static HowayResult createSuccessResult(Object data){
        return createSuccessResult("",data);
    }

    public static HowayResult createFailResult(StatusCode status,String condition,Object data){
        return createResult(status,condition,data);
    }
    public static HowayResult createFailResult(StatusCode status,Object data){
        return createFailResult(status,"",data);
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getCondition() {
        return condition;
    }

    public Object getData() {
        return data;
    }
    public boolean isOk(){
        return this.statusCode.getCode() == StatusCode.SUCCESS.getCode();
    }

    @Override
    public String toString(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("code",statusCode.getCode());
        map.put("msg",statusCode.getMsg());
        map.put("condition",condition);
        map.put("data",JSON.toJSON(data));
        return JSON.toJSONString(map);
    }
}

package cn.ihoway.common;


import cn.ihoway.annotation.Processor;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.common.io.CommonOutput;
import cn.ihoway.security.HowayAccessToken;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * 程序处理器公共类，所有逻辑处理器必须继承该类
 * @param <I>
 * @param <O>
 */
public abstract class CommonProcessor<I extends CommonInput,O extends CommonOutput> {

    private final HowayLog logger = new HowayLog(CommonProcessor.class);
    private final HowayAccessToken accessToken = new HowayAccessToken();
    protected static final String APP_KEY = "5582";
    protected static final String APP_SECRET = "d1497b84";
    /**
     * 数据检查
     * @param input input
     * @return HowayResult
     */
    protected StatusCode dataCheck(I input){
        return StatusCode.SUCCESS;
    }

    /**
     * 安全认证
     * @param input input
     * @return StatusCode
     */
    protected StatusCode certification(I input, AuthorityLevel limitAuthority) {
        return accessToken.isToekenRule(input.token,limitAuthority);
    }

    /**
     * 程序执行之前
     * @param input
     * @param output
     * @return
     */
    protected HowayResult beforeProcess(I input, O output){
        return HowayResult.createSuccessResult(output);
    }

    /**
     * 程序主要执行部分
     * @param input input
     * @param output output
     * @return HowayResult
     */
    protected abstract HowayResult process(I input,O output);

    /**
     * 程序执行之后
     * @param input input
     * @param output output
     * @return HowayResult
     */
    protected HowayResult afterPrcocess(I input,O output){
        return HowayResult.createSuccessResult(output);
    }

    public HowayResult doExcute(I input,O output){
        try{
            Processor annotation = this.getClass().getAnnotation(Processor.class);
            logger.info("begin --> input: "+ JSON.toJSONString(input));
            StatusCode sc = dataCheck(input);
            if( sc.getCode() != StatusCode.SUCCESS.getCode() ){
                return HowayResult.createFailResult(sc,"数据检查失败",output);
            }
            if(annotation.certification()){
                sc = certification(input,annotation.limitAuthority());
                if( sc.getCode() != StatusCode.SUCCESS.getCode() ){
                    return HowayResult.createFailResult(sc,"安全认证失败",output);
                }
            }
            HowayResult response = beforeProcess(input,output);
            if(!response.isOk()){
                return response;
            }
            response = process(input,output);
            if(!response.isOk()){
                return response;
            }
            response = afterPrcocess(input,output);
            if(!response.isOk()){
                return response;
            }
            logger.info("end --> response: "+ JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            logger.error(Arrays.toString(e.getStackTrace()));
            return HowayResult.createFailResult(StatusCode.JAVAEXCEPTION,output);
        }
    }


}

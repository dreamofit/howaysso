package cn.ihoway.common;


import cn.ihoway.annotation.Processor;
import cn.ihoway.api.record.RecordAsm;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.common.io.CommonOutput;
import cn.ihoway.container.HowayContainer;
import cn.ihoway.security.HowayAccessToken;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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
        return accessToken.isToekenRule(input.token,limitAuthority.getLevel());
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
        if(StringUtils.isNotBlank(input.traceId)){
            MDC.put("traceId",input.traceId);
        }

        if(StringUtils.isBlank(input.eventNo)){
            logger.info("事件编号不能为空!");
            return HowayResult.createFailResult(StatusCode.FIELDMISSING,"事件编号不能为空!",output);
        }
        HowayResult response = inserRecord(input,output); //记录input日志
        if(response.getStatusCode() == StatusCode.DUPLICATREQUEST){
            return response;
        }
        response = getResponse(input,output);
        updateRecord(input,response); //记录output日志
        logger.info("end --> response: "+ JSON.toJSONString(response));
        return response;
    }

    private HowayResult getResponse(I input,O output){
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

    /**
     * 写日志 ———— input情况
     * @param input
     * @param output
     * @return
     */
    private HowayResult inserRecord(I input,O output){
        try{
            RecordAsm recordAsm = (RecordAsm) HowayContainer.getContext().getBean("RecordAsm");
            HashMap<String,Object> res = recordAsm.findByEventNo(input.eventNo);
            if(res != null){
                logger.info("请求重复！eventNo:" + input.eventNo);
                String resOutput = (String) res.get("output");
                return HowayResult.createFailResult(StatusCode.DUPLICATREQUEST,"请求重复!",StringUtils.isBlank(resOutput)?output:JSON.parse(resOutput));
            }
            HashMap<String,String> addInput = new HashMap<>();
            addInput.put("eventNo",input.eventNo);
            addInput.put("input",JSON.toJSONString(input));
            addInput.put("inputToken",input.token);
            long timeStamp = System.currentTimeMillis();
            Date date = new Date(timeStamp);
            addInput.put("inputTime",getCurrentTime(date));
            addInput.put("inputTimestamp",String.valueOf(timeStamp));
            addInput.put("sysName","howaysso");
            addInput.put("ip",input.ip);
            addInput.put("method",input.method);
            recordAsm.addRecor(addInput);
        }catch (Exception e){
            logger.error("[warning] input日志写入失败，cause by :" + e.getCause());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return HowayResult.createSuccessResult(output);
    }

    private void updateRecord(I input,HowayResult response){
        try {
            RecordAsm recordAsm = (RecordAsm) HowayContainer.getContext().getBean("RecordAsm");
            HashMap<String,String> updateInput = new HashMap<>();
            updateInput.put("eventNo",input.eventNo);
            updateInput.put("output",JSON.toJSONString(response));
            long timeStamp = System.currentTimeMillis();
            Date date = new Date(timeStamp);
            updateInput.put("outputTime",getCurrentTime(date));
            updateInput.put("outputTimestamp",String.valueOf(timeStamp));
            updateInput.put("responseCode",String.valueOf(response.getStatusCode().getCode()));
            recordAsm.updateRecord(updateInput);
        }catch (Exception e){
            logger.error("[warning] output日志写入失败，cause by :" + e.getCause());
            logger.error(Arrays.toString(e.getStackTrace()));
        }

    }

    private String getCurrentTime(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 获取事件编号
     * @return
     */
    private static String getEventNo(){
        return HowayEncrypt.encrypt(UUID.randomUUID().toString(),"MD5",12);
    }
}

package cn.ihoway.common.io;


import cn.ihoway.common.CommonSeria;

/**
 * 【强制】 所有input类必须继承CommonInput
 * 【强制】 所有input类内部类必须继承CommonSeria
 */
public class CommonInput extends CommonSeria {
    public String token;
    public String eventNo;
    public String ip;
    public String method;
    public String traceId;
}

package cn.ihoway.type;

/**
 * 状态码
 */
public enum StatusCode {
    SUCCESS(200,"成功"),
    INSERTERROR(1002,"插入数据失败"),
    UPDATEERROR(1003,"更新数据失败"),
    DELETEERROR(1004,"删除失败"),
    ILLEGALPARAMETER(2001,"非法参数"),
    DUPLICATENAME(2002,"重复用户名"),
    DUPLICATTEL(2003,"该电话号已被使用"),
    FIELDMISSING(2004,"必填字段为空"),
    USEREMPTY(3001,"用户不存在"),
    PASSWORDERROR(3002,"密码错误"),
    VERFICATIONCODEERROR(3003,"验证码错误"),
    PERMISSIONDENIED(3004,"权限不足"),
    USERSTATUSERROR(3005,"用户状态异常，请联系管理员处理"),
    TOKENERROR(3006,"token检查失败"),
    TOKENTIMEOUT(3007,"token超时，请重新登录"),
    LOGINTYPENOTSURPPORT(3008,"登录方式暂不支持"),
    SEARCHTYPENOTSUPPORT(3009,"查询方式暂不支持"),
    JAVAEXCEPTION(4001,"java异常，请联系管理员处理"),
    ILLEGALOPERATION(5001,"非法操作"),
    DUPLICATREQUEST(6001,"重复请求");

    private int code;
    private String msg;
    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() { return this.code; }
    public String getMsg() { return this.msg; }

}

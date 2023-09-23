package cn.ihoway.type;

/**
 * 状态码
 */
public enum StatusCode {
    SUCCESS(200,"成功"),
    INSERT_ERROR(1002,"插入数据失败"),
    UPDATE_ERROR(1003,"更新数据失败"),
    DELETE_ERROR(1004,"删除失败"),
    ILLEGAL_PARAMETER(2001,"非法参数"),
    DUPLICATE_NAME(2002,"重复用户名"),
    DUPLICATE_TEL(2003,"该电话号已被使用"),
    FIELD_MISSING(2004,"必填字段为空"),
    USER_EMPTY(3001,"用户不存在"),
    PASSWORD_ERROR(3002,"密码错误"),
    VERIFICATION_CODE_ERROR(3003,"验证码错误"),
    PERMISSION_DENIED(3004,"权限不足"),
    USER_STATUS_ERROR(3005,"用户状态异常，请联系管理员处理"),
    TOKEN_ERROR(3006,"token检查失败"),
    TOKEN_TIMEOUT(3007,"token超时，请重新登录"),
    LOGIN_TYPE_NOT_SURPPORT(3008,"登录方式暂不支持"),
    SEARCH_TYPE_NOT_SUPPORT(3009,"查询方式暂不支持"),
    JAVA_EXCEPTION(4001,"系统异常，请联系管理员处理"),
    CONFIG_ERROR(4002,"配置异常，请联系管理员处理"),
    ILLEGAL_OPERATION(5001,"非法操作"),
    DUPLICATE_REQUEST(6001,"重复请求");

    private int code;
    private String msg;
    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() { return this.code; }
    public String getMsg() { return this.msg; }

}

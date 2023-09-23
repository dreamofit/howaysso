package cn.ihoway.type;

/**
 * 登录方式，目前仅支持用户名密码登录和电话密码登录
 */
public enum LoginType {
    NAME_AND_PASS, //用户名密码登录
    TEL_ONLY, //电话验证登录
    TEL_AND_PASS, //电话密码登录
    NAME_AND_EMAIL //用户名邮箱验证登录
}

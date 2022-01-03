package cn.ihoway.type;

/**
 * 登录方式，目前仅支持用户名密码登录和电话密码登录
 */
public enum LoginType {
    NAMEANDPASS, //用户名密码登录
    TELONLY, //电话验证登录
    TELANDPASS, //电话密码登录
    NAMEANDEMAIL //用户名邮箱验证登录
}

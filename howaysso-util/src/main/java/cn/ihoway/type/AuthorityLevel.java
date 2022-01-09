package cn.ihoway.type;

/**
 * 权限等级：如果设置一个网站的权限等级，则只有对应权限及以上的用户允许访问
 */
public enum AuthorityLevel {
    SUPERADMIN(999), //超级管理员
    ADMINISTRATOR(99), //管理员
    SUPERMEMBER(9), //超级会员
    MEMBER(3), //会员
    COMMONMEMBER(2), //普通用户
    LIMITMEMBER(1), //限制用户 部分授权网页不可访问
    FORBIDDENMEMBER(0); //永禁用户（黑名单用户），全部授权网页不可访问，网页等级为0代表不授权网页，即使不登录也可以进行访问

    private int level;//等级

    public int getLevel() {
        return level;
    }

    AuthorityLevel(int level){
        this.level = level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

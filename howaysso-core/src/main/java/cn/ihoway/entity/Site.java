package cn.ihoway.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * site
 * @author howay
 */
@Data
public class Site implements Serializable {
    private Integer id;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 具体链接地址
     */
    private String url;

    /**
     * 0-999,对应用户权限，为最低进入权限
     * 999：超级管理员 99：管理员 9：超级会员 3：普通会员 2：普通用户 1：限制用户 0:永禁用户
     */
    private Integer rank;

    /**
     * 是否启用，0-未启用，1-启用
     */
    private Integer enable;

    private String appkey;

    private String appsecret;

    /**
     * 备用字段
     */
    private String backup;

    private static final long serialVersionUID = 1L;
}
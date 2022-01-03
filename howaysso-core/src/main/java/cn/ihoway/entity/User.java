package cn.ihoway.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author howay
 */
@Data
public class User implements Serializable {
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码——md5加密
     */
    private String password;

    /**
     * 电话
     */
    private String tel;

    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 个人网站
     */
    private String site;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 999：超级管理员 99：管理员 9：超级会员 3：普通会员 2：普通用户 1：限制用户 0:永禁用户
     */
    private Integer role;

    /**
     * 备用字段
     */
    private String backup;

    private static final long serialVersionUID = 1L;
}
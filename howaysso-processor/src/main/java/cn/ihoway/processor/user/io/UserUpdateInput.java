package cn.ihoway.processor.user.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class UserUpdateInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {

        public Integer uid;
        /**
         * 以下都是基本信息修改，只允许本人进行修改
         * 其中name为特殊，与用户密码相关联，更新名字则必输密码，故暂时不允许修改
         */
        public String name;
        public String tel;
        public String qq;
        public String email;
        public String site;
        public String sign;

        /** 以下信息只允许管理员进行修改 */
        public Integer role;
    }
}

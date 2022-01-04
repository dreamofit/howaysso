package cn.ihoway.processor.user.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class UserUpdateInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public String name;
        public String tel;
        public String qq;
        public String email;
        public String site;
        public String sign;
        public Integer role;
    }
}

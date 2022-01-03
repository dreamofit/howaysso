package cn.ihoway.processor.user.io;


import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.type.LoginType;

import java.io.Serializable;

public class UserLoginInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public String name;
        public String password;
        public String tel;
        public String email;
        public LoginType loginType;
    }
}

package cn.ihoway.processor.user.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.type.UserSearchType;

public class UserSearchInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public UserSearchType type;
        public Integer uid; //查询用户id
    }
}

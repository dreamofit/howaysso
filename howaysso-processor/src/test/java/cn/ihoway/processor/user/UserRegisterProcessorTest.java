package cn.ihoway.processor.user;

import cn.ihoway.processor.user.io.UserRegisterInput;
import cn.ihoway.processor.user.io.UserRegisterOutput;
import cn.ihoway.util.HowayResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRegisterProcessorTest {
    private UserRegisterProcessor processor = new UserRegisterProcessor();

    @Test
    void process() {
        UserRegisterInput input = new UserRegisterInput();
        input.inChomm.name = "李四";
        input.inChomm.tel = "15215675779";
        input.inChomm.password = "123456";
        UserRegisterOutput output = new UserRegisterOutput();
        HowayResult result = processor.doExcute(input,output);
        System.out.println(result.toString());
        assert result.isOk();
    }
}
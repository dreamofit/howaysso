package cn.ihoway.processor.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.processor.user.io.UserSearchInput;
import cn.ihoway.processor.user.io.UserSearchOutput;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayResult;

@Processor(name = "UserNoTokenSearchProcessor")
public class UserNoTokenSearchProcessor extends CommonProcessor<UserSearchInput, UserSearchOutput> {
    private final UserSearchProcessor userSearchProcessor = new UserSearchProcessor();

    @Override
    protected StatusCode dataCheck(UserSearchInput input){
        return userSearchProcessor.dataCheck(input);
    }

    @Override
    protected HowayResult process(UserSearchInput input, UserSearchOutput output) {
        return userSearchProcessor.process(input,output);
    }
}

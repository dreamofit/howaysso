package cn.ihoway.processor.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.processor.user.io.UserUpdateInput;
import cn.ihoway.processor.user.io.UserUpdateOutput;
import cn.ihoway.service.UserService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 管理员更新用户role
 */
@Processor(name = "UserUpdateRoleProcessor",certification = true,limitAuthority = AuthorityLevel.ADMINISTRATOR)
public class UserUpdateRoleProcessor extends CommonProcessor<UserUpdateInput, UserUpdateOutput> {

    private final HowayLog logger = new HowayLog(UserUpdateRoleProcessor.class);
    private final UserService service = (UserServiceImpl) HowayContainer.getBean("userServiceImpl");

    @Override
    protected StatusCode dataCheck(UserUpdateInput input){
        if(StringUtils.isEmpty(input.token) || input.inChomm.uid == null || input.inChomm.role == null){
            return StatusCode.FIELD_MISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(UserUpdateInput input, UserUpdateOutput output) {
        User user = service.findById(input.inChomm.uid);
        if (user == null) {
            return HowayResult.createFailResult(StatusCode.USER_EMPTY, output);
        }
        user.setRole(input.inChomm.role);
        User operateor;
        try {
            operateor = accessToken.getUserByToken(input.token);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return HowayResult.createFailResult(StatusCode.JAVA_EXCEPTION,output);
        }
        if(service.updateUser(user) == 0){
            logger.info("更新成功");
        }
        output.token = accessToken.getToken(operateor.getId(),operateor.getName(),user.getPassword(),APP_KEY,APP_SECRET);
        return HowayResult.createSuccessResult(output);
    }
}

package cn.ihoway.processor.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.processor.user.io.UserUpdateInput;
import cn.ihoway.processor.user.io.UserUpdateOutput;
import cn.ihoway.service.UserService;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * 用户本人信息修改处理器
 */
@Processor(name = "UserUpdateProcessor",certification = true)
public class UserUpdateProcessor extends CommonProcessor<UserUpdateInput, UserUpdateOutput> {
    private final HowayLog logger = new HowayLog(UserUpdateProcessor.class);
    private final UserService service = (UserServiceImpl) HowayContainer.getBean("userServiceImpl");

    @Override
    protected StatusCode dataCheck(UserUpdateInput input){
        if(StringUtils.isEmpty(input.token)){
            return StatusCode.FIELD_MISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(UserUpdateInput input, UserUpdateOutput output) {
        User user;
        try {
            user = accessToken.getUserByToken(input.token);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            return HowayResult.createFailResult(StatusCode.JAVA_EXCEPTION,output);
        }
        //todo 更新名字也需要更新密码
        if(StringUtils.isNotEmpty(input.inChomm.name)){
            User temp = service.findByName(input.inChomm.name);
            if(temp != null){
                return HowayResult.createFailResult(StatusCode.DUPLICATE_NAME,output);
            }
            user.setPassword(HowayEncrypt.md5(input.inChomm.name + user.getPassword()));
            user.setName(input.inChomm.name);

        }
        if(StringUtils.isNotEmpty(input.inChomm.tel)){
            User temp = service.findByTel(input.inChomm.tel);
            if(temp != null){
                return HowayResult.createFailResult(StatusCode.DUPLICATE_TEL,output);
            }
            user.setTel(input.inChomm.tel);
        }
        if(StringUtils.isNotEmpty(input.inChomm.email)){
            user.setEmail(input.inChomm.email);
        }
        if(StringUtils.isNotEmpty(input.inChomm.qq)){
            user.setQq(input.inChomm.qq);
        }
        if(StringUtils.isNotEmpty(input.inChomm.sign)){
            user.setSign(input.inChomm.sign);
        }
        if(StringUtils.isNotEmpty(input.inChomm.site)){
            user.setSite(input.inChomm.site);
        }
        if(input.inChomm.role != null){
            user.setRole(input.inChomm.role);
        }
        if(service.updateUser(user) == 0){
            logger.info("更新成功");
        }
        output.token = accessToken.getToken(user.getId(),user.getName(),user.getPassword(),APP_KEY,APP_SECRET);
        return HowayResult.createSuccessResult(output);
    }
}

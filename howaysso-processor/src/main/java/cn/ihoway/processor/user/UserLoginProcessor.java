package cn.ihoway.processor.user;


import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.processor.user.io.UserLoginInput;
import cn.ihoway.processor.user.io.UserLoginOutput;
import cn.ihoway.service.UserService;
import cn.ihoway.type.LoginType;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户登录处理器 目前仅支持用户名和密码登录和电话号和密码登录
 */
@Processor(name = "UserLoginProcessor")
public class UserLoginProcessor extends CommonProcessor<UserLoginInput, UserLoginOutput> {

    private final UserService service = (UserServiceImpl) HowayContainer.getBean("userServiceImpl");
    private final HowayLog logger = new HowayLog(UserLoginProcessor.class);

    @Override
    protected StatusCode dataCheck(UserLoginInput input){
        if( input.inChomm.loginType == null ){
            input.inChomm.loginType = LoginType.NAME_AND_PASS;
        }
        if( input.inChomm.loginType == LoginType.NAME_AND_PASS){
            if(StringUtils.isEmpty(input.inChomm.name) || StringUtils.isEmpty(input.inChomm.password) ){
                logger.info("用户名和密码不能为空");
                return StatusCode.FIELD_MISSING;
            }
        }else if( input.inChomm.loginType == LoginType.TEL_AND_PASS){
            if(StringUtils.isEmpty(input.inChomm.tel) || StringUtils.isEmpty(input.inChomm.password) ){
                logger.info("电话号和密码不能为空");
                return StatusCode.FIELD_MISSING;
            }
        }else if( input.inChomm.loginType == LoginType.TEL_ONLY){
            if(StringUtils.isEmpty(input.inChomm.tel) ){
                logger.info("电话号不能为空");
                return StatusCode.FIELD_MISSING;
            }
        }else if( input.inChomm.loginType == LoginType.NAME_AND_EMAIL){
            if(StringUtils.isEmpty(input.inChomm.name) || StringUtils.isEmpty(input.inChomm.email) ){
                logger.info("用户名和邮箱不能为空");
                return StatusCode.FIELD_MISSING;
            }
        }
        return StatusCode.SUCCESS;
    }

    /**
     * 主程序
     * @param input input
     * @param output output
     * @return HowayResult
     */
    @Override
    protected HowayResult process(UserLoginInput input, UserLoginOutput output) {
        return switch (input.inChomm.loginType) {
            case NAME_AND_PASS -> userIsExist(input, output);
            case TEL_AND_PASS -> telIsExist(input, output);
            default -> HowayResult.createFailResult(StatusCode.LOGIN_TYPE_NOT_SURPPORT, output);
        };
    }

    /**
     * 电话是否存在
     * @param input input
     * @param output output
     * @return HowayResult
     */
    private HowayResult telIsExist(UserLoginInput input, UserLoginOutput output) {
        User user = service.findByTel(input.inChomm.tel);
        //service.free();
        return getUserExist(input, output, user);
    }

    /**
     * 用户名是否存在
     * @param input input
     * @param output output
     * @return HowayResult
     */
    private HowayResult userIsExist(UserLoginInput input, UserLoginOutput output) {
        User user = service.findByName(input.inChomm.name);
        //service.free();
        return getUserExist(input, output, user);
    }

    /**
     * 如果用户不存在则报错，否则对密码进行加密处理
     * @param input input
     * @param output output
     * @param user user
     * @return res
     */
    private HowayResult getUserExist(UserLoginInput input, UserLoginOutput output, User user) {
        if( user == null ){
            return HowayResult.createFailResult(StatusCode.USER_EMPTY, output);
        }
        if(StringUtils.isNotEmpty(input.inChomm.password)){
            input.inChomm.password = HowayEncrypt.md5(user.getName()+input.inChomm.password);
            if(!input.inChomm.password.equals(user.getPassword())){
                return HowayResult.createFailResult(StatusCode.PASSWORD_ERROR,output);
            }
        }
        //logger.info("uid:"+user.getId() + " name:"+user.getName() + " pass:"+user.getPassword());
        output.token = accessToken.getToken(user.getId(),user.getName(),user.getPassword(),APP_KEY,APP_SECRET);
        return HowayResult.createSuccessResult(output);
    }


    /**
     * 是否允许登录，如果用户名和电话允许重复则需要进行用户查询和用户加密码查询两个步骤，
     * 但此系统设计时不允许用户名和电话重复，故可以省略这步
     * @param input input
     * @return true / false
     */
    private boolean isCanLogin(UserLoginInput input,UserLoginOutput output){
        return switch (input.inChomm.loginType) {
            case NAME_AND_PASS -> nameAndPass(input,output);
            case TEL_AND_PASS -> telAndPass(input,output);
            default -> false;
        };
    }

    private boolean nameAndPass(UserLoginInput input,UserLoginOutput output) {
        User user = service.loginByNameAndPass(input.inChomm.name,input.inChomm.password);
        if(user != null){
            output.token = accessToken.getToken(user.getId(),user.getName(),user.getPassword(),APP_KEY,APP_SECRET);
        }
        return user != null;
    }

    private boolean telAndPass(UserLoginInput input,UserLoginOutput output){
        User user = service.loginByTelAndPass(input.inChomm.tel,input.inChomm.password);
        if(user != null){
            output.token = accessToken.getToken(user.getId(),user.getName(),user.getPassword(),APP_KEY,APP_SECRET);
        }
        return user != null;
    }

}

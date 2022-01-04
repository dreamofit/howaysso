package cn.ihoway.processor.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.processor.user.io.UserRegisterInput;
import cn.ihoway.processor.user.io.UserRegisterOutput;
import cn.ihoway.service.UserService;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang.StringUtils;

/**
 * 用户注册处理器
 */
@Processor(name = "UserRegisterProcessor")
public class UserRegisterProcessor extends CommonProcessor<UserRegisterInput, UserRegisterOutput> {

    private final UserService service = new UserServiceImpl();
    private final HowayLog logger = new HowayLog(UserRegisterProcessor.class);

    @Override
    protected StatusCode dataCheck(UserRegisterInput input){
        if(StringUtils.isEmpty(input.inChomm.name)||StringUtils.isEmpty(input.inChomm.password)||StringUtils.isEmpty(input.inChomm.tel)){
            logger.info("用户名、密码或者电话有空值");
            return StatusCode.FIELDMISSING;
        }
        // todo 手机号格式判断、邮箱格式判断（前端也需要进行控制）
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult beforeProcess(UserRegisterInput input, UserRegisterOutput output){
        User user = service.findByName(input.inChomm.name);
        User user2 = service.findByTel(input.inChomm.tel);
        if( user == null && user2 == null ){
            input.inChomm.password = HowayEncrypt.md5(input.inChomm.name+input.inChomm.password);//对密码进行md5加密，前端也需要进行md5加密
            return HowayResult.createSuccessResult(output);
        }else{
            if( user2 == null ){
                logger.info("用户名重复！");
                return HowayResult.createFailResult(StatusCode.DUPLICATENAME,output);
            }else{
                logger.info("电话号重复");
                return HowayResult.createFailResult(StatusCode.DUPLICATTEL,output);
            }
        }

    }


    @Override
    protected HowayResult process(UserRegisterInput input, UserRegisterOutput output) {
        logger.info("input: "+input.toString());
        User user = packToUser(input);
        int r = service.addUser(user);
        if( r != 1){
            logger.info("插入失败");
            return HowayResult.createFailResult(StatusCode.INSERTERROR,output);
        }
        logger.info("插入用户成功");
        return HowayResult.createSuccessResult(output);
    }

    private User packToUser(UserRegisterInput input){
        Convert convert = new Convert();
        return (User) convert.inputToBean(User.class.getName(),UserRegisterInput.class.getName()+"$InChomm",input.inChomm);
    }

}

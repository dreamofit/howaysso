package cn.ihoway.processor.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.User;
import cn.ihoway.impl.UserServiceImpl;
import cn.ihoway.processor.user.io.UserSearchInput;
import cn.ihoway.processor.user.io.UserSearchOutput;
import cn.ihoway.security.HowayAccessToken;
import cn.ihoway.service.UserService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询搜索处理器，支持单个查询（仅id）和多个查询
 */
@Processor(name = "UserSearchProcessor",certification = true)
public class UserSearchProcessor extends CommonProcessor<UserSearchInput, UserSearchOutput> {
    private final UserService service = new UserServiceImpl();

    @Override
    protected StatusCode dataCheck(UserSearchInput input){
        if(input.inChomm.type == null){
            return StatusCode.FIELDMISSING;
        }
        if(input.inChomm.type == UserSearchType.ONLYID){
            if(input.inChomm.uid == null){
                return StatusCode.FIELDMISSING;
            }
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected StatusCode certification(UserSearchInput input, AuthorityLevel limitAuthority) {
        HowayAccessToken accessToken = new HowayAccessToken();
        if(input.inChomm.type == UserSearchType.ALL){
            //查询所有需要管理员权限
            return accessToken.isToekenRule(input.token,AuthorityLevel.ADMINISTRATOR.getLevel());
        }
        return accessToken.isToekenRule(input.token,limitAuthority.getLevel());
    }

    @Override
    protected HowayResult process(UserSearchInput input, UserSearchOutput output) {
        List<User> userList = new ArrayList<>();
        if(input.inChomm.type == UserSearchType.ONLYID){
            User user = service.findById(input.inChomm.uid);
            if(user != null){
                userList.add(user);
            }
        }else if(input.inChomm.type == UserSearchType.ALL){
            userList = service.findAll();
        }else{
            return HowayResult.createFailResult(StatusCode.SEARCHTYPENOTSUPPORT, output);
        }
        for (User user:userList){
            try {
                User temp = (User) user.clone();
                temp.setPassword(null);
                output.userList.add(temp);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        service.free();
        return HowayResult.createSuccessResult(output);
    }

}

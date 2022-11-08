package cn.ihoway.controller.user;

import cn.ihoway.processor.user.*;
import cn.ihoway.processor.user.io.*;
import cn.ihoway.type.LoginType;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayRequest;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final HowayLog logger = new HowayLog(UserController.class);

    /**
     * 用户登录接口
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public String login(@RequestBody JSONObject user, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        UserLoginProcessor loginProcessor = new UserLoginProcessor();
        UserLoginInput input = new UserLoginInput();
        input.inChomm.name = user.getString("name");
        input.inChomm.tel = user.getString("tel");
        if(StringUtils.isNotBlank(input.inChomm.name)){
            input.inChomm.loginType = LoginType.NAMEANDPASS;
        }else if(StringUtils.isNotBlank(input.inChomm.tel)){
            input.inChomm.loginType = LoginType.TELANDPASS;
        }
        input.inChomm.password = user.getString("password");
        input.eventNo = user.getString("eventNo");
        input.ip = HowayRequest.getIpAddr(request);
        UserLoginOutput output = new UserLoginOutput();
        HowayResult rs = loginProcessor.doExecute(input,output);
        try {
            output = (UserLoginOutput) rs.getData();
            session.setAttribute("howay_token", output.token);
            Cookie tokenCookie = new Cookie("howay_token", output.token);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(-1);
            response.addCookie(tokenCookie);
        }catch (Exception e){
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return rs.toString();
    }

    /**
     * 用户注册接口
     */
    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add(@RequestBody JSONObject user,HttpServletRequest request){
        UserRegisterInput input = new UserRegisterInput();
        input.inChomm.name = user.getString("name");
        input.inChomm.password = user.getString("password");
        input.inChomm.tel = user.getString("tel");
        input.inChomm.email = user.getString("email");
        input.inChomm.site = user.getString("site");
        input.eventNo = user.getString("eventNo");
        input.ip = HowayRequest.getIpAddr(request);
        UserRegisterProcessor registerProcessor = new UserRegisterProcessor();
        UserRegisterOutput output = new UserRegisterOutput();
        return registerProcessor.doExecute(input,output).toString();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public String update(String token,@RequestBody JSONObject user,HttpServletRequest request){
        UserUpdateProcessor updateProcessor = new UserUpdateProcessor();
        UserUpdateInput input = new UserUpdateInput();
        UserUpdateOutput output = new UserUpdateOutput();
        input.token = token;
        input.inChomm.qq = user.getString("qq");
        input.inChomm.sign = user.getString("sign");
        input.inChomm.tel = user.getString("tel");
        // input.inChomm.name = user.getString("name"); 暂时不支持更改名字，更改名字需要更改密码
        input.inChomm.email = user.getString("email");
        input.inChomm.site = user.getString("site");
        input.eventNo = user.getString("eventNo");
        input.ip = HowayRequest.getIpAddr(request);
        HowayResult rs = updateProcessor.doExecute(input,output);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.PUT })
    public String updateRole(String token,@PathVariable("uid") Integer uid,@RequestBody JSONObject user,HttpServletRequest request){
        UserUpdateRoleProcessor updateRoleProcessor = new UserUpdateRoleProcessor();
        UserUpdateInput input = new UserUpdateInput();
        UserUpdateOutput output = new UserUpdateOutput();
        input.token = token;
        input.inChomm.uid = uid;
        input.inChomm.role = user.getInteger("role");
        input.eventNo = user.getString("eventNo");
        input.ip = HowayRequest.getIpAddr(request);
        HowayResult rs = updateRoleProcessor.doExecute(input,output);
        return rs.toString();
    }


    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.GET })
    public String selectAllUser(String token,String eventNo,HttpServletRequest request){
        UserSearchProcessor searchProcessor = new UserSearchProcessor();
        UserSearchInput input = new UserSearchInput();
        input.inChomm.type = UserSearchType.ALL;
        input.token = token;
        input.eventNo = eventNo;
        input.ip = HowayRequest.getIpAddr(request);
        UserSearchOutput output = new UserSearchOutput();
        return searchProcessor.doExecute(input,output).toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.GET })
    public String selectByUid(String token,String eventNo,@PathVariable("uid") Integer uid,HttpServletRequest request){
        UserSearchProcessor searchProcessor = new UserSearchProcessor();
        UserSearchInput input = new UserSearchInput();
        input.inChomm.type = UserSearchType.ONLYID;
        input.inChomm.uid = uid;
        input.token = token;
        input.eventNo = eventNo;
        input.ip = HowayRequest.getIpAddr(request);
        UserSearchOutput output = new UserSearchOutput();
        return searchProcessor.doExecute(input,output).toString();
    }


}

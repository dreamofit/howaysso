package cn.ihoway.controller.user;

import cn.ihoway.processor.user.*;
import cn.ihoway.processor.user.io.*;
import cn.ihoway.type.LoginType;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @CrossOrigin
    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public String login(@RequestBody JSONObject user, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        UserLoginProcessor loginProcessor = new UserLoginProcessor();
        UserLoginInput input = new UserLoginInput();
        if(StringUtils.isEmpty(user.getString("loginType"))){
            input.inChomm.loginType = LoginType.NAMEANDPASS;
        }else if(Integer.parseInt(user.getString("loginType")) > 3 || Integer.parseInt(user.getString("loginType"))<0) {
            input.inChomm.loginType = LoginType.NAMEANDPASS;
        }else{
            input.inChomm.loginType = LoginType.values()[Integer.parseInt(user.getString("loginType"))];
        }
        input.inChomm.name = user.getString("name");
        input.inChomm.password = user.getString("password");
        input.inChomm.tel = user.getString("tel");
        UserLoginOutput output = new UserLoginOutput();
        HowayResult rs = loginProcessor.doExcute(input,output);
        output = (UserLoginOutput) rs.getData();
        session.setAttribute("howay_token", output.token);
        Cookie tokenCookie = new Cookie("howay_token", output.token);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(-1);
        response.addCookie(tokenCookie);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.POST })
    public String add(@RequestBody JSONObject user){
        UserRegisterInput input = new UserRegisterInput();
        input.inChomm.name = user.getString("name");
        input.inChomm.password = user.getString("password");
        input.inChomm.tel = user.getString("tel");
        input.inChomm.email = user.getString("email");
        input.inChomm.site = user.getString("site");
        UserRegisterProcessor registerProcessor = new UserRegisterProcessor();
        UserRegisterOutput output = new UserRegisterOutput();
        return registerProcessor.doExcute(input,output).toString();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public String update(String token,@RequestBody JSONObject user){
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
        HowayResult rs = updateProcessor.doExcute(input,output);
        return rs.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.PUT })
    public String updateRole(String token,@PathVariable("uid") Integer uid,@RequestBody JSONObject user){
        UserUpdateRoleProcessor updateRoleProcessor = new UserUpdateRoleProcessor();
        UserUpdateInput input = new UserUpdateInput();
        UserUpdateOutput output = new UserUpdateOutput();
        input.token = token;
        input.inChomm.uid = uid;
        input.inChomm.role = user.getInteger("role");
        HowayResult rs = updateRoleProcessor.doExcute(input,output);
        return rs.toString();
    }


    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.GET })
    public String selectAllUser(String token){
        UserSearchProcessor searchProcessor = new UserSearchProcessor();
        UserSearchInput input = new UserSearchInput();
        input.inChomm.type = UserSearchType.ALL;
        input.token = token;
        UserSearchOutput output = new UserSearchOutput();
        return searchProcessor.doExcute(input,output).toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.GET })
    public String selectByUid(String token,@PathVariable("uid") Integer uid){
        UserSearchProcessor searchProcessor = new UserSearchProcessor();
        UserSearchInput input = new UserSearchInput();
        input.inChomm.type = UserSearchType.ONLYID;
        input.inChomm.uid = uid;
        input.token = token;
        UserSearchOutput output = new UserSearchOutput();
        return searchProcessor.doExcute(input,output).toString();
    }


}

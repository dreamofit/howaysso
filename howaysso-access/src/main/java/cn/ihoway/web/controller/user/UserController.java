package cn.ihoway.web.controller.user;

import cn.ihoway.processor.user.io.*;
import cn.ihoway.type.LoginType;
import cn.ihoway.type.UserSearchType;
import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final HowayLog logger = new HowayLog(UserController.class);

    /**
     * 用户登录接口
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public String login2(@RequestBody JSONObject user, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isNotBlank(user.getString("name"))){
            user.put("loginType",LoginType.NAME_AND_PASS);
        }else if(StringUtils.isNotBlank(user.getString("tel"))){
            user.put("loginType",LoginType.TEL_AND_PASS);
        }
        HowayResult rs = AccessRoute.handle(request,"login",user);
        try {
            UserLoginOutput output = (UserLoginOutput) rs.getData();
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
        HowayResult rs = AccessRoute.handle(request,"register",user);
        return rs.toString();
    }

    /**
     * 用户本人进行信息修改操作
     */
    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.PUT })
    public String update(@RequestBody JSONObject user,HttpServletRequest request){
        HowayResult rs = AccessRoute.handle(request,"userUpdate",user);
        return rs.toString();
    }

    /**
     * 管理员更新用户的权限
     */
    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.PUT })
    public String updateRole(@PathVariable("uid") int uid,@RequestBody JSONObject user,HttpServletRequest request){
        user.put("uid",uid);
        user.put("role",Integer.parseInt(user.getString("role")));
        HowayResult rs = AccessRoute.handle(request,"userUpdateRole",user);
        return rs.toString();
    }


    /**
     * 查询全部用户信息
     * @param token 事件编号
     * @param eventNo 事件编号
     * @param request 请求
     */
    @CrossOrigin
    @RequestMapping(value = "", method = { RequestMethod.GET })
    public String selectAllUser(String token,String eventNo,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("type",UserSearchType.ALL);
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"userSearch",map);
        return rs.toString();
    }

    /**
     * 查询某个uid的信息
     * @param token 事件编号
     * @param eventNo 事件编号
     * @param uid 用户id
     * @param request 请求
     */
    @CrossOrigin
    @RequestMapping(value = "/{uid}", method = { RequestMethod.GET })
    public String selectByUid(String token,String eventNo,@PathVariable("uid") Integer uid,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("type",UserSearchType.ONLY_ID);
        map.put("uid",uid);
        map.put("token",token);
        map.put("eventNo",eventNo);
        HowayResult rs = AccessRoute.handle(request,"userSearch",map);
        return rs.toString();
    }


}

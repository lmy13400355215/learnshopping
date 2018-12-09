package com.neuedu.controller.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {

    @Autowired
    IUserService userService;
    /**
     * 登录
     * */
    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session,String username, String password){

        ServerResponse serverResponse= userService.login(username,password);
        if (serverResponse.isSuccess()){//保存登录状态
            session.setAttribute(Const.CURRENTUSER,serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo){

        return userService.register(userInfo);
    }

    /**
     * 校验用户名和邮箱是否有效
     */

    @RequestMapping(value = "check_valid.do")
    public ServerResponse check_valid(String str,String type){
        return userService.check_valid(str,type);
    }

    /**
     * 获取用户登录信息
     */

    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        Object o=session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo= (UserInfo) o;
            UserInfo responseUserInfo=new UserInfo();
            responseUserInfo.setId(userInfo.getId());
            responseUserInfo.setUsername(userInfo.getUsername());
            responseUserInfo.setEmail(userInfo.getEmail());
            responseUserInfo.setCreateTime(userInfo.getCreateTime());
            responseUserInfo.setUpdateTime(userInfo.getUpdateTime());
            return ServerResponse.createServerResponseBySucess(null,responseUserInfo);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }
    /**
     * 获取用户登录用户的详细信息
     */
    @RequestMapping(value = "get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session){
        Object o=session.getAttribute(Const.CURRENTUSER);
        if (o!=null && o instanceof UserInfo){
            UserInfo userInfo= (UserInfo) o;

            return ServerResponse.createServerResponseBySucess(null,userInfo);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }

    /**
     * 根据用户名获取密保问题
     */
    @RequestMapping(value = "forget_get_question.do")
    public ServerResponse forget_get_question(String username){

        return userService.forget_get_question(username);
    }
    /**
     * 提交问题答案接口
     */
    @RequestMapping(value = "forget_check_answer.do")
    public ServerResponse forget_check_answer(String username,String question,String answer){
        return userService.forget_check_answer(username,question,answer);
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "forget_reset_password.do")
    public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken){
        return userService.forget_reset_password(username,passwordNew,forgetToken);
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createServerResponseBySucess();

    }

    /**
     * 登录状态下重置密码
     */

    @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
        Object o=session.getAttribute(Const.CURRENTUSER);
        if (o!=null&&o instanceof UserInfo){
            UserInfo userInfo= (UserInfo) o;
            return userService.reset_password(userInfo,passwordOld,passwordNew);
        }
        return ServerResponse.createServerResponseByError(ResponseCode.USER_NOT_LOGIN.getStatus(),ResponseCode.USER_NOT_LOGIN.getMsg());
    }


}

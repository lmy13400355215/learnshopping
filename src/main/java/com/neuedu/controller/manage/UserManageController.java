package com.neuedu.controller.manage;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/user/")
public class UserManageController {
    @Autowired
    IUserService userService;

    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session, String username, String password){

        ServerResponse serverResponse= userService.login(username,password);
        if (serverResponse.isSuccess()){//保存登录状态
            UserInfo userInfo= (UserInfo) serverResponse.getData();
            if (userInfo.getRole()==Const.USER_ROLE_CUSTOMER){
                return ServerResponse.createServerResponseByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,serverResponse.getData());
        }
        return serverResponse;
    }
}

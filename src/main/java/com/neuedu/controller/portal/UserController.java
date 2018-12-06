package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {

    @Autowired
    IUserService userService;
    /**
     * 登录
     * */
    @RequestMapping(value = "login.do")
    public ServerResponse login(String username,String password){

        return userService.login(username,password);
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserInfo userInfo){

        return userService.register(userInfo);
    }

}

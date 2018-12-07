package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

import javax.servlet.http.HttpSession;

public interface IUserService {


    /**
     * 登录
     */
    public ServerResponse login(String username,String password);

    /**
     * 注册接口
     */
    public ServerResponse register(UserInfo userInfo);

    /**
     * 校验用户名和邮箱是否有效
     */
    public ServerResponse check_valid(String str,String type);

/**
 * 根据用户名获取密保问题
 */
    public ServerResponse forget_get_question(String username);
    /**
     * 提交问题答案接口
     */
    ServerResponse forget_check_answer(String username,String question,String answer);
    /**
     * 忘记密码的重置密码
     */
    ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken);

    /**
     *登录状态下修改
     */
    ServerResponse reset_password(UserInfo userInfo, String passwordOld, String passwordNew);
}

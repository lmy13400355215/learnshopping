package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

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
}

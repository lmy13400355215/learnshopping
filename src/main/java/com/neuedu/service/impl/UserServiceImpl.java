package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse login(String username, String password) {

        //step1:参数非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError("用户不能为空");
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByError("密码不能为空");
        }


        //step2:检查username是否存在
        int result=userInfoMapper.checkUsername(username);
        if (result<=0){//用户名不存在
            return ServerResponse.createServerResponseByError("用户名不存在");
        }
        //step3:根据用户名和密码查询
        UserInfo userInfo=userInfoMapper.selectByUsernameAndPassword(username,password);
        if (userInfo==null){//密码错误
            return ServerResponse.createServerResponseByError("密码错误");
        }
        //step4:处理结果并返回
        //前端返回数据时 要把密码置空
        userInfo.setPassword("");
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //step1:参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByError("参数必需");
        }
        //step2:校验用户名
        int result=userInfoMapper.checkUsername(userInfo.getUsername());
        if (result>0){//用户名已存在
            return ServerResponse.createServerResponseByError("用户名已存在");
        }
        //step3:校验邮箱
        int result_email=userInfoMapper.checkEmail(userInfo.getEmail());
        if(result_email>0){//邮箱存在
            return ServerResponse.createServerResponseByError("邮箱已存在");
        }
        //step4:注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        int count=userInfoMapper.insert(userInfo);
        if (count>0){
            return ServerResponse.createServerResponseBySucess("注册成功");
        }
        //step5:返回结果
        return ServerResponse.createServerResponseByError("注册失败");
    }

}

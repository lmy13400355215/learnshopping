package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.TokenCache;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     *登录接口
     */
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

       /* int result=userInfoMapper.checkUsername(username);
        if (result<=0){//用户名不存在
            return ServerResponse.createServerResponseByError("用户名不存在");
        }*/

        ServerResponse serverResponse=check_valid(username,Const.USERNAME);
        if (serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());
        }
        //step3:根据用户名和密码查询
        UserInfo userInfo=userInfoMapper.selectByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        if (userInfo==null){//密码错误
            return ServerResponse.createServerResponseByError("密码错误");
        }
        //step4:处理结果并返回
        //前端返回数据时 要把密码置空
        userInfo.setPassword("");
        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }

    /**
     *注册接口
     */
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //step1:参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY.getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:校验用户名

        /*int result=userInfoMapper.checkUsername(userInfo.getUsername());
        if (result>0){//用户名已存在
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }*/

        String username=userInfo.getUsername();
        ServerResponse serverResponse=check_valid(username,Const.USERNAME);
        if (!serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
        }


        //step3:校验邮箱
        /*int result_email=userInfoMapper.checkEmail(userInfo.getEmail());
        if(result_email>0){//邮箱存在
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }*/

        ServerResponse email_serverResponse=check_valid(userInfo.getEmail(),Const.EMAIL);
        if (!email_serverResponse.isSuccess()){
            return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
        }
        //step4:注册
        userInfo.setRole(Const.USER_ROLE_CUSTOMER);
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int count=userInfoMapper.insert(userInfo);
        if (count>0){
            return ServerResponse.createServerResponseBySucess("注册成功");
        }
        //step5:返回结果
        return ServerResponse.createServerResponseByError("注册失败");
    }

    /**
     * 检查用户或邮箱是否有效的接口
     */

    @Override
    public ServerResponse check_valid(String str, String type) {
        //step1:参数非空校验
        if (StringUtils.isBlank(str)||StringUtils.isBlank(type)){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY .getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:判断用户名或者邮箱是否有效
        if (type.equals(Const.USERNAME)){
            int username_result=userInfoMapper.checkUsername(str);
            if (username_result>0){
                return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_USERNAME.getStatus(),ResponseCode.EXISTS_USERNAME.getMsg());
            }
            return ServerResponse.createServerResponseBySucess("成功");
        }else if(type.equals(Const.EMAIL)){
            int email_result=userInfoMapper.checkEmail(str);
            if (email_result>0){
                return ServerResponse.createServerResponseByError(ResponseCode.EXISTS_EMAIL.getStatus(),ResponseCode.EXISTS_EMAIL.getMsg());
            }
            return ServerResponse.createServerResponseBySucess("成功");
        }

        //step3:返回结果

        return ServerResponse.createServerResponseByError("type参数传递有误");
    }

    /**
     * 根据用户名获取密保问题
     */

    @Override
    public ServerResponse forget_get_question(String username) {
        //step1:参数非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY .getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:判断用户名是否存在
        ServerResponse serverResponse=check_valid(username,Const.USERNAME);
        if (serverResponse.getStatus()!=ResponseCode.EXISTS_USERNAME.getStatus()){//用户名不存在
            return ServerResponse.createServerResponseByError(ResponseCode.NOT_EXISTS_USERNAME.getStatus(),ResponseCode.NOT_EXISTS_USERNAME.getMsg());
        }
        //step3:查询密保问题
        String question=userInfoMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByError("密保问题为空");
        }
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(null,question);
    }

    /**
     *提交问题答案
     */
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1:参数非空校验
        if (StringUtils.isBlank(username)||StringUtils.isBlank(question)||StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //step2:校验答案
        int count=userInfoMapper.checkAnswerByUsernameAndQuestion(username,question,answer);
        if (count<=0){
            return ServerResponse.createServerResponseByError("答案错误");
        }
        //返回用户的唯一标识-->username-->token
        //UUID生成一个不会重复的字符串
        String user_token=UUID.randomUUID().toString();
        TokenCache.put(username,user_token);
        //step3:返回结果
        return ServerResponse.createServerResponseBySucess(null,user_token);
    }

    /**
     *修改密码
     */
    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken) {
        //step1:参数非空校验
        if (StringUtils.isBlank(username)||StringUtils.isBlank(passwordNew)||StringUtils.isBlank(forgetToken)){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY .getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }

        //step3:校验token
        String token=TokenCache.get(username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createServerResponseByError("token不存在或者过期");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByError("token不一致");

        }
        //step2:更新密码
        int count=userInfoMapper.updatePasswordByUsername(username,MD5Utils.getMD5Code(passwordNew));
        if (count<=0){
            return ServerResponse.createServerResponseByError("密码修改失败");
        }
        //step3:返回结果

        return ServerResponse.createServerResponseBySucess();
    }

    /**
     *登录状态下重置密码
     */
    @Override
    public ServerResponse reset_password(UserInfo userInfo, String passwordOld, String passwordNew) {

        //step1:参数非空校验
        if (StringUtils.isBlank(passwordOld)||StringUtils.isBlank(passwordNew)){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY .getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:校验旧密码是否正确
        UserInfo userInfoOld=userInfoMapper.selectByUsernameAndPassword(userInfo.getUsername(),MD5Utils.getMD5Code(passwordOld));
        if (userInfoOld==null){
            return ServerResponse.createServerResponseByError("旧密码错误");
        }
        //step3:修改密码
        int count=userInfoMapper.updatePasswordByUsername(userInfo.getUsername(),MD5Utils.getMD5Code(passwordNew));

        //step4:返回结果

        if (count<=0){
            return ServerResponse.createServerResponseByError("密码修改失败");

        }
        return ServerResponse.createServerResponseBySucess();
    }



}

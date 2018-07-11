package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2018/7/7.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    //用户登录
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.CheckUserName(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //判断密码
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        //判断登录是否成功
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }


    //用户注册
    public ServerResponse<String> register(User user) {
        ServerResponse<String> username = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!username.isSuccess()) {
            return username;
        }

        ServerResponse<String> email = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!email.isSuccess()) {
            return email;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //用户名和邮箱校验
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.CheckUserName(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已经存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.CheckEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email已经存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //忘记密码
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse<String> response = this.checkValid(username, Const.CURRENT_USER);
        if(!response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUserName(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("密码问题为空");

    }

    //找回密码答案是否正确
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount>0){
            String token = UUID.randomUUID().toString();
            TokenCache.setKey("token_"+username,token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }
}

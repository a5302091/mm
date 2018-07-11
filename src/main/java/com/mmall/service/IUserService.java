package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Administrator on 2018/7/7.
 */
public interface IUserService {

    //用户登录
    ServerResponse<User> login(String username, String password);

    //用户注册
    ServerResponse<String> register(User user);

    //校验用户名和邮箱是否存在
    ServerResponse<String> checkValid(String str, String type);

    //找回密码
    ServerResponse<String> selectQuestion(String username);

    //找回密码答案
    ServerResponse<String> checkAnswer(String username,String question,String answer);



}

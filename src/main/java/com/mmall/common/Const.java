package com.mmall.common;

/**
 * Created by Administrator on 2018/7/8.
 */
public class Const {
    public static final String CURRENT_USER="CURRENT_USER";//当前 用户

    public static final String EMAIL="email";
    public static final String USERNAME="username";

    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }
}

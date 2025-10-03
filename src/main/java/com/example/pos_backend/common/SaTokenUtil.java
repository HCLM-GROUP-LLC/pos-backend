package com.example.pos_backend.common;

import cn.dev33.satoken.stp.StpUtil;

/**
 * sa令牌工具
 *
 * @author hanhua
 * @since 2025/10/03
 */
public class SaTokenUtil {
    private static final String USER_TYPE="userType";
    /**
     * 登录
     *
     * @param id         用户ID
     * @param clientType 客户端类型
     * @param userType   用户类型
     */
    public static void login(String id,ClientTypeEnum clientType,UserTypeEnum userType){
        StpUtil.login(id, clientType.getCode());
        //标记用户类型
        StpUtil.getSession().set(USER_TYPE,userType.getCode());
    }

    /**
     * 获取用户类型
     *
     * @return {@link UserTypeEnum }
     */
    public static UserTypeEnum getUserType(){
        String userType = StpUtil.getSession().getString(USER_TYPE);
        return UserTypeEnum.getByCode(userType);
    }
}

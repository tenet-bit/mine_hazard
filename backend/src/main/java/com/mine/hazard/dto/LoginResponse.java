package com.mine.hazard.dto;

import lombok.Data;

/** 登录成功响应 */
@Data
public class LoginResponse {

    /** JWT Token */
    private String token;

    /** Token 类型，固定为 Bearer */
    private String tokenType = "Bearer";

    /** Token 过期时间（毫秒） */
    private Long expiresIn;

    /** 用户基本信息 */
    private UserInfoDTO userInfo;

    public LoginResponse(String token, Long expiresIn, UserInfoDTO userInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
}

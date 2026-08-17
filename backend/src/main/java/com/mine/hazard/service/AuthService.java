package com.mine.hazard.service;

import com.mine.hazard.dto.LoginRequest;
import com.mine.hazard.dto.LoginResponse;
import com.mine.hazard.dto.UserInfoDTO;

public interface AuthService {

    /** 用户登录，验证用户名密码，返回 JWT Token 和用户信息 */
    LoginResponse login(LoginRequest request);

    /** 获取当前登录用户的详细信息（含菜单树） */
    UserInfoDTO getCurrentUserInfo(String username);
}

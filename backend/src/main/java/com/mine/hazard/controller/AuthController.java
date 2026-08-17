package com.mine.hazard.controller;

import com.mine.hazard.common.Result;
import com.mine.hazard.dto.LoginRequest;
import com.mine.hazard.dto.LoginResponse;
import com.mine.hazard.dto.UserInfoDTO;
import com.mine.hazard.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 * POST /api/auth/login  - 登录
 * GET  /api/auth/me     - 获取当前用户信息
 * POST /api/auth/logout - 退出（JWT 无状态，前端清除 Token 即可）
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息（含菜单树）
     */
    @GetMapping("/me")
    public Result<UserInfoDTO> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.unauthorized("未登录");
        }
        String username = authentication.getName();
        UserInfoDTO userInfo = authService.getCurrentUserInfo(username);
        return Result.success(userInfo);
    }

    /**
     * 退出登录（JWT 无状态，仅通知前端清除 Token）
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("用户退出登录: {}", authentication.getName());
        }
        SecurityContextHolder.clearContext();
        return Result.success();
    }
}

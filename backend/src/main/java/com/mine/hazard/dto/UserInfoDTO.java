package com.mine.hazard.dto;

import lombok.Data;

import java.util.List;

/** 当前登录用户信息（含菜单树） */
@Data
public class UserInfoDTO {

    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;

    /** 角色编码列表，如 ["ADMIN"] */
    private List<String> roles;

    /** 菜单树（用于前端侧边栏渲染） */
    private List<MenuDTO> menus;
}

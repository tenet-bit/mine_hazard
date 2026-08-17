package com.mine.hazard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mine.hazard.dto.*;
import com.mine.hazard.entity.SysMenu;
import com.mine.hazard.entity.SysRole;
import com.mine.hazard.entity.SysUser;
import com.mine.hazard.mapper.SysMenuMapper;
import com.mine.hazard.mapper.SysRoleMapper;
import com.mine.hazard.mapper.SysUserMapper;
import com.mine.hazard.security.JwtUtil;
import com.mine.hazard.security.LoginUser;
import com.mine.hazard.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // 使用 Spring Security 认证（会调用 UserDetailsService + BCrypt 校验密码）
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            SysUser user = loginUser.getUser();

            // 生成 JWT Token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());

            // 构建用户信息（含菜单树）
            UserInfoDTO userInfo = buildUserInfo(user, loginUser.getRoleCodes());

            log.info("用户登录成功: {} 角色: {}", user.getUsername(), loginUser.getRoleCodes());
            return new LoginResponse(token, jwtUtil.getExpiration(), userInfo);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码错误");
        } catch (AuthenticationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserInfoDTO getCurrentUserInfo(String username) {
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, 0));

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<String> roleCodes = roleMapper.selectRolesByUserId(user.getId())
                .stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());

        return buildUserInfo(user, roleCodes);
    }

    /** 构建用户信息 DTO（含菜单树） */
    private UserInfoDTO buildUserInfo(SysUser user, List<String> roleCodes) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setRoles(roleCodes);

        // 查询该用户有权限的菜单，构建树形结构
        List<SysMenu> menus = menuMapper.selectMenusByUserId(user.getId());
        dto.setMenus(buildMenuTree(menus, 0L));
        return dto;
    }

    /** 将平铺菜单列表构建为树形结构 */
    private List<MenuDTO> buildMenuTree(List<SysMenu> menus, Long parentId) {
        List<MenuDTO> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                MenuDTO node = toMenuDTO(menu);
                List<MenuDTO> children = buildMenuTree(menus, menu.getId());
                node.setChildren(children.isEmpty() ? null : children);
                tree.add(node);
            }
        }
        return tree;
    }

    private MenuDTO toMenuDTO(SysMenu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setParentId(menu.getParentId());
        dto.setMenuName(menu.getMenuName());
        dto.setMenuType(menu.getMenuType());
        dto.setPath(menu.getPath());
        dto.setComponent(menu.getComponent());
        dto.setIcon(menu.getIcon());
        dto.setSortOrder(menu.getSortOrder());
        dto.setPermission(menu.getPermission());
        return dto;
    }
}

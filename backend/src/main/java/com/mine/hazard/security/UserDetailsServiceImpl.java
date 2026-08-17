package com.mine.hazard.security;

import com.mine.hazard.entity.SysUser;
import com.mine.hazard.mapper.SysRoleMapper;
import com.mine.hazard.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetailsService 实现
 * 从数据库加载用户及其角色信息
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用专用方法查询用户（含密码字段，绕过 @TableField(select=false) 限制）
        SysUser user = userMapper.selectByUsernameWithPassword(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 查询用户角色编码
        List<String> roleCodes = roleMapper.selectRolesByUserId(user.getId())
                .stream()
                .map(role -> role.getRoleCode())
                .collect(Collectors.toList());

        return new LoginUser(user, roleCodes);
    }
}

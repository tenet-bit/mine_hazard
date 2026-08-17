package com.mine.hazard.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mine.hazard.entity.SysUser;
import com.mine.hazard.entity.SysUserRole;
import com.mine.hazard.mapper.SysUserMapper;
import com.mine.hazard.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统数据初始化器
 * 应用启动后执行：若默认用户不存在，则创建四个默认账号并绑定角色
 * BCrypt 密码由 Spring PasswordEncoder 在运行时生成，保证正确性
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        long userCount = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeleted, 0));

        if (userCount > 0) {
            log.info("[DataInitializer] 默认用户已存在，跳过初始化");
            return;
        }

        log.info("[DataInitializer] 开始初始化默认用户...");

        // admin / admin123 / ADMIN (role_id=1)
        createUser("admin",    "admin123", "系统管理员", "admin@mine.com",    "13800000001", 1L);
        // operator / admin123 / MONITOR (role_id=2)
        createUser("operator", "admin123", "监测值班员", "operator@mine.com", "13800000002", 2L);
        // engineer / admin123 / ENGINEER (role_id=3)
        createUser("engineer", "admin123", "防治水工程师", "engineer@mine.com", "13800000003", 3L);
        // manager / admin123 / MANAGER (role_id=4)
        createUser("manager",  "admin123", "矿井管理人员", "manager@mine.com",  "13800000004", 4L);

        log.info("[DataInitializer] 默认用户初始化完成");
    }

    private void createUser(String username, String rawPassword, String realName,
                            String email, String phone, Long roleId) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRealName(realName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(1);
        user.setDeleted(0);
        userMapper.insert(user);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);

        log.info("[DataInitializer] 创建用户: {} -> 角色ID: {}", username, roleId);
    }
}

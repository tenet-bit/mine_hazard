package com.mine.hazard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mine.hazard.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户（含密码字段，用于 Spring Security 认证）
     * 绕过 @TableField(select=false) 限制
     */
    @Select("SELECT id, username, password, real_name, email, phone, avatar, status, deleted " +
            "FROM sys_user WHERE username = #{username} AND deleted = 0")
    SysUser selectByUsernameWithPassword(String username);
}

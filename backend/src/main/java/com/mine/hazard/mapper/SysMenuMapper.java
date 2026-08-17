package com.mine.hazard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mine.hazard.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询其拥有的所有菜单（通过角色关联）
     * 只返回目录(M)和菜单(C)类型，按排序字段升序
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "  AND m.deleted = 0 " +
            "  AND m.visible = 1 " +
            "  AND m.menu_type IN ('M', 'C') " +
            "ORDER BY m.parent_id ASC, m.sort_order ASC")
    List<SysMenu> selectMenusByUserId(Long userId);
}

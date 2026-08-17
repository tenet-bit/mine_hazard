package com.mine.hazard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单权限表
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父菜单ID，0表示顶级 */
    private Long parentId;

    /** 菜单名称 */
    private String menuName;

    /** 菜单类型：M=目录 C=菜单 F=按钮 */
    private String menuType;

    /** 路由路径 */
    private String path;

    /** 组件路径 */
    private String component;

    /** Element Plus 图标名称 */
    private String icon;

    /** 显示排序 */
    private Integer sortOrder;

    /** 权限标识 */
    private String permission;

    /** 是否显示：0-隐藏 1-显示 */
    private Integer visible;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String remark;
}

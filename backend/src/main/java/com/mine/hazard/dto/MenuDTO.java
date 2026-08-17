package com.mine.hazard.dto;

import lombok.Data;

import java.util.List;

/** 菜单节点 DTO（树形结构） */
@Data
public class MenuDTO {

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private String permission;

    /** 子菜单列表 */
    private List<MenuDTO> children;
}

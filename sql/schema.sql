-- ============================================================
-- 矿用水害防治系统 - Phase 1 数据库结构
-- 字符集: utf8mb4 / utf8mb4_unicode_ci
-- ============================================================

CREATE DATABASE IF NOT EXISTS mine_hazard
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE mine_hazard;

-- ============================================================
-- 1. 用户表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username    VARCHAR(50)     NOT NULL                COMMENT '登录用户名',
    password    VARCHAR(100)    NOT NULL                COMMENT 'BCrypt加密密码',
    real_name   VARCHAR(50)     DEFAULT NULL            COMMENT '真实姓名',
    email       VARCHAR(100)    DEFAULT NULL            COMMENT '邮箱',
    phone       VARCHAR(20)     DEFAULT NULL            COMMENT '手机号',
    avatar      VARCHAR(500)    DEFAULT NULL            COMMENT '头像URL',
    status      TINYINT         NOT NULL DEFAULT 1      COMMENT '状态 0-禁用 1-启用',
    deleted     TINYINT         NOT NULL DEFAULT 0      COMMENT '逻辑删除 0-正常 1-已删除',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500)    DEFAULT NULL            COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ============================================================
-- 2. 角色表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_name   VARCHAR(50)     NOT NULL                COMMENT '角色名称',
    role_code   VARCHAR(50)     NOT NULL                COMMENT '角色编码 (ADMIN/MONITOR/ENGINEER/MANAGER)',
    description VARCHAR(200)    DEFAULT NULL            COMMENT '描述',
    sort_order  INT             NOT NULL DEFAULT 0      COMMENT '排序',
    status      TINYINT         NOT NULL DEFAULT 1      COMMENT '状态 0-禁用 1-启用',
    deleted     TINYINT         NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- ============================================================
-- 3. 用户角色关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id      BIGINT  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT  NOT NULL               COMMENT '用户ID',
    role_id BIGINT  NOT NULL               COMMENT '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ============================================================
-- 4. 菜单权限表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_menu (
    id          BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    parent_id   BIGINT          NOT NULL DEFAULT 0      COMMENT '父菜单ID（0表示顶级）',
    menu_name   VARCHAR(50)     NOT NULL                COMMENT '菜单名称',
    menu_type   CHAR(1)         NOT NULL DEFAULT 'C'    COMMENT '类型 M=目录 C=菜单 F=按钮',
    path        VARCHAR(200)    DEFAULT NULL            COMMENT '路由路径',
    component   VARCHAR(200)    DEFAULT NULL            COMMENT '组件路径',
    icon        VARCHAR(100)    DEFAULT NULL            COMMENT '图标名称(Element Plus图标)',
    sort_order  INT             NOT NULL DEFAULT 0      COMMENT '显示排序',
    permission  VARCHAR(100)    DEFAULT NULL            COMMENT '权限标识',
    visible     TINYINT         NOT NULL DEFAULT 1      COMMENT '是否显示 0-隐藏 1-显示',
    deleted     TINYINT         NOT NULL DEFAULT 0      COMMENT '逻辑删除',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500)    DEFAULT NULL            COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_menu_type (menu_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- ============================================================
-- 5. 角色菜单关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id      BIGINT  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT  NOT NULL               COMMENT '角色ID',
    menu_id BIGINT  NOT NULL               COMMENT '菜单ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    KEY idx_role_id (role_id),
    KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- ============================================================
-- 6. 操作日志表
-- ============================================================
CREATE TABLE IF NOT EXISTS sys_oper_log (
    id             BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id        BIGINT          DEFAULT NULL            COMMENT '操作用户ID',
    username       VARCHAR(50)     DEFAULT NULL            COMMENT '操作用户名',
    operation      VARCHAR(100)    DEFAULT NULL            COMMENT '操作描述',
    method         VARCHAR(200)    DEFAULT NULL            COMMENT '请求方法',
    request_url    VARCHAR(500)    DEFAULT NULL            COMMENT '请求URL',
    request_method VARCHAR(10)     DEFAULT NULL            COMMENT 'HTTP方法',
    request_param  TEXT            DEFAULT NULL            COMMENT '请求参数',
    response_result TEXT           DEFAULT NULL            COMMENT '返回结果',
    status         TINYINT         NOT NULL DEFAULT 0      COMMENT '操作状态 0-成功 1-失败',
    error_msg      TEXT            DEFAULT NULL            COMMENT '错误消息',
    oper_ip        VARCHAR(50)     DEFAULT NULL            COMMENT '操作IP',
    oper_time      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    cost_time      BIGINT          DEFAULT 0               COMMENT '耗时(ms)',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_oper_time (oper_time),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

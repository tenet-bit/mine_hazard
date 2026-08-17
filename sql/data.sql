-- ============================================================
-- 矿用水害防治系统 - Phase 1 初始化数据
-- 包含：角色、菜单、角色菜单关联
-- 用户数据由 Spring Boot DataInitializer 创建（BCrypt加密）
-- ============================================================

USE mine_hazard;

-- ============================================================
-- 初始化角色（4个系统角色）
-- ============================================================
INSERT INTO sys_role (id, role_name, role_code, description, sort_order, status)
VALUES
(1, '系统管理员', 'ADMIN',    '拥有系统全部权限',   1, 1),
(2, '监测值班员', 'MONITOR',  '负责实时监测和预警处理', 2, 1),
(3, '防治水工程师', 'ENGINEER', '负责地质管理和工程管控', 3, 1),
(4, '矿井管理人员', 'MANAGER',  '负责综合报表和总览审批', 4, 1)
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name), description=VALUES(description);

-- ============================================================
-- 初始化菜单（完整的7个模块 + 子菜单）
-- 菜单类型：M=目录（父节点）  C=菜单（叶子节点）
-- ============================================================

-- 一级目录
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(100, 0, '实时监测中心', 'M', '/monitor',    NULL, 'Monitor',      1,  NULL, 1),
(200, 0, '智能预警中心', 'M', '/alarm',      NULL, 'Bell',         2,  NULL, 1),
(300, 0, '水文地质管理', 'M', '/geology',    NULL, 'MapLocation',  3,  NULL, 1),
(400, 0, '防治水工程',   'M', '/project',    NULL, 'Setting',      4,  NULL, 1),
(500, 0, '巡检统计分析', 'M', '/inspection', NULL, 'Search',       5,  NULL, 1),
(600, 0, '综合报表',     'M', '/report',     NULL, 'DataAnalysis', 6,  NULL, 1),
(700, 0, '系统管理',     'M', '/system',     NULL, 'Tools',        7,  NULL, 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), icon=VALUES(icon), sort_order=VALUES(sort_order);

-- 二级菜单：实时监测中心
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(101, 100, '实时监测总览', 'C', '/monitor/overview', 'monitor/MonitorOverview', 'View',     1, 'monitor:view',   1),
(102, 100, '历史数据',     'C', '/monitor/history',  'monitor/MonitorHistory',  'DataLine', 2, 'monitor:history',1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：智能预警中心
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(201, 200, '预警规则管理', 'C', '/alarm/rule',   'alarm/AlarmRule',   'SetUp',       1, 'alarm:rule',   1),
(202, 200, '预警记录处理', 'C', '/alarm/record', 'alarm/AlarmRecord', 'WarningFilled', 2, 'alarm:record', 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：水文地质管理
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(301, 300, '含水层管理', 'C', '/geology/aquifer',  'geology/Aquifer',  'Opportunity', 1, 'geology:aquifer',  1),
(302, 300, '隔水层管理', 'C', '/geology/aquitard', 'geology/Aquitard', 'Grid',        2, 'geology:aquitard', 1),
(303, 300, '地质台账',   'C', '/geology/fracture', 'geology/Fracture', 'Document',    3, 'geology:fracture', 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：防治水工程
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(401, 400, '工程列表', 'C', '/project/list',     'project/ProjectList', 'List',      1, 'project:list',     1),
(402, 400, '排水设施', 'C', '/project/drainage', 'project/Drainage',   'Operation', 2, 'project:drainage', 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：巡检统计分析
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(501, 500, '巡检计划', 'C', '/inspection/plan',   'inspection/InspectionPlan',   'Calendar',     1, 'inspection:plan',   1),
(502, 500, '巡检记录', 'C', '/inspection/record', 'inspection/InspectionRecord', 'Tickets',      2, 'inspection:record', 1),
(503, 500, '巡检统计', 'C', '/inspection/stat',   'inspection/InspectionStat',   'TrendCharts',  3, 'inspection:stat',   1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：综合报表
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(601, 600, '数据大屏', 'C', '/report/dashboard', 'report/Dashboard', 'Monitor', 1, 'report:dashboard', 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- 二级菜单：系统管理
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible)
VALUES
(701, 700, '用户管理', 'C', '/system/user', 'system/UserManage', 'User',       1, 'system:user', 1),
(702, 700, '角色管理', 'C', '/system/role', 'system/RoleManage', 'UserFilled', 2, 'system:role', 1)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name), path=VALUES(path), component=VALUES(component);

-- ============================================================
-- 角色菜单关联
-- ADMIN(1)      : 所有菜单
-- MONITOR(2)    : 实时监测 + 智能预警 + 巡检统计
-- ENGINEER(3)   : 实时监测 + 水文地质 + 防治水工程 + 巡检统计 + 综合报表
-- MANAGER(4)    : 实时监测 + 智能预警 + 巡检统计 + 综合报表
-- ============================================================

-- ADMIN 全部菜单
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
VALUES
(1,100),(1,101),(1,102),
(1,200),(1,201),(1,202),
(1,300),(1,301),(1,302),(1,303),
(1,400),(1,401),(1,402),
(1,500),(1,501),(1,502),(1,503),
(1,600),(1,601),
(1,700),(1,701),(1,702);

-- MONITOR：实时监测 + 智能预警 + 巡检统计
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
VALUES
(2,100),(2,101),(2,102),
(2,200),(2,201),(2,202),
(2,500),(2,501),(2,502),(2,503);

-- ENGINEER：实时监测 + 水文地质 + 防治水工程 + 巡检统计 + 综合报表
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
VALUES
(3,100),(3,101),(3,102),
(3,300),(3,301),(3,302),(3,303),
(3,400),(3,401),(3,402),
(3,500),(3,501),(3,502),(3,503),
(3,600),(3,601);

-- MANAGER：实时监测 + 智能预警 + 巡检统计 + 综合报表
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
VALUES
(4,100),(4,101),(4,102),
(4,200),(4,201),(4,202),
(4,500),(4,501),(4,502),(4,503),
(4,600),(4,601);

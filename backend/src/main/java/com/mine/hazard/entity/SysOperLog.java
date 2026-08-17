package com.mine.hazard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/** 操作日志表 */
@Data
@TableName("sys_oper_log")
public class SysOperLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String requestUrl;
    private String requestMethod;
    private String requestParam;
    private String responseResult;

    /** 0-成功 1-失败 */
    private Integer status;
    private String errorMsg;
    private String operIp;
    private LocalDateTime operTime;
    private Long costTime;
}

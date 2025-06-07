package com.github.zephyrtoria.task_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName tasks
 */
@TableName(value ="tasks")
@Data
public class Tasks implements Serializable {
    private Long id;

    private Long projectId;

    private String title;

    private String content;

    private Integer status;

    private Integer priority;

    private Date createdTime;

    private static final long serialVersionUID = 1L;
}
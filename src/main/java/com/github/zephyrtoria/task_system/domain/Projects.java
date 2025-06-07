package com.github.zephyrtoria.task_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName projects
 */
@TableName(value ="projects")
@Data
public class Projects implements Serializable {
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;
}
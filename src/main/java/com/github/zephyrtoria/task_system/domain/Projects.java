package com.github.zephyrtoria.task_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName projects
 */
@TableName(value ="projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projects implements Serializable {
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;
}
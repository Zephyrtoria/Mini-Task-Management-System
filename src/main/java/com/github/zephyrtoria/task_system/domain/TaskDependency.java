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
 * @TableName task_dependency
 */
@TableName(value ="task_dependency")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDependency implements Serializable {
    private Long prevId;

    private Long nextId;

    private static final long serialVersionUID = 1L;
}
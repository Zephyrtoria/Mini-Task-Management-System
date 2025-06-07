package com.github.zephyrtoria.task_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName user_project
 */
@TableName(value ="user_project")
@Data
public class UserProject implements Serializable {
    private Long userId;

    private Long projectId;

    private Integer level;

    private static final long serialVersionUID = 1L;
}
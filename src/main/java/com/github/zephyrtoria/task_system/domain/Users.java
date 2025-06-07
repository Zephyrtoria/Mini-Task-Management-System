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
 * @TableName users
 */
@TableName(value ="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private Long id;

    private String name;

    private String password;

    private static final long serialVersionUID = 1L;
}
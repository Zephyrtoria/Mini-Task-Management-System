package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

@Data
public class TasksSetStatusDTO {
    private Long id;

    private Long projectId;

    private Integer status;
}

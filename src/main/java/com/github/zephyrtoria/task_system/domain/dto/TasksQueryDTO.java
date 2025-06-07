package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

@Data
public class TasksQueryDTO {
    private Long projectId;

    private Integer type;

    private Integer status;

    private Integer priority;
}

package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

@Data
public class TasksQueryDTO {
    private Long projectId;

    private Integer type = 0;

    private Integer status = 0;

    private Integer priority = 0;
}

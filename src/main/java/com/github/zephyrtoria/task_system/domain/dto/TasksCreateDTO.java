package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TasksCreateDTO {
    private Long projectId;

    private String title;

    private String content;

    private Integer status;

    private Integer priority;

    private List<Long> prevTasksIds;
}

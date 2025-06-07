package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TasksUpdateDTO {
    private Long id;

    private Long projectId;

    private String title;

    private String content;

    private Integer priority;

    private List<Long> prevTasksIds;
}

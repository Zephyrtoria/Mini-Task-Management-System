package com.github.zephyrtoria.task_system.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TasksQueryVO {
    private Long id;

    private Long projectId;

    private String title;

    private String content;

    private Integer status;

    private Integer priority;

    private Date createdTime;

    private List<Long> prevTasksIds;
}

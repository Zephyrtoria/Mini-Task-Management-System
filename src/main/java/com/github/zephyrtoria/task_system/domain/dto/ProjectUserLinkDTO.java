package com.github.zephyrtoria.task_system.domain.dto;

import lombok.Data;

@Data
public class ProjectUserLinkDTO {
    private Long projectId;
    private Long userId;
    private Integer level;
}

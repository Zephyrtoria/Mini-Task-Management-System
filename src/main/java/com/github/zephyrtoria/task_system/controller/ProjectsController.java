package com.github.zephyrtoria.task_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects")
@Slf4j
@Tag(name = "项目接口")
public class ProjectsController {
}

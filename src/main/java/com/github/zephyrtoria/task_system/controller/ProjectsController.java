package com.github.zephyrtoria.task_system.controller;

import com.github.zephyrtoria.task_system.domain.dto.ProjectCreateDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUpdateDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUserLinkDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.service.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projects")
@Slf4j
@Tag(name = "项目接口")
public class ProjectsController {
    @Resource
    private ProjectsService projectsService;

    @PostMapping("")
    @Operation(description = "新建项目")
    public Result insert(@RequestBody ProjectCreateDTO projectCreateDTO) {
        log.info("新建项目:  {}", projectCreateDTO);
        return projectsService.insert(projectCreateDTO);
    }

    @PutMapping("")
    @Operation(description = "修改项目")
    public Result modify(@RequestBody ProjectUpdateDTO projectUpdateDTO) {
        log.info("修改项目:  {}", projectUpdateDTO);
        return projectsService.modify(projectUpdateDTO);
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除项目")
    public Result delete(@PathVariable Long id) {
        log.info("删除项目:  {}", id);
        return projectsService.delete(id);
    }

    @GetMapping("")
    @Operation(description = "查看所有项目")
    public Result queryAll() {
        log.info("查看所有项目");
        return projectsService.queryAll();
    }

    @GetMapping("{id}")
    @Operation(description = "查看当前项目")
    public Result query(@PathVariable Long id) {
        log.info("查看当前项目:  {}", id);
        return projectsService.queryById(id);
    }

    @GetMapping("user/{id}")
    @Operation(description = "查看项目包含用户")
    public Result queryUsers(@PathVariable Long id) {
        log.info("查看项目包含用户:  {}", id);
        return projectsService.queryUsers(id);
    }

    @PostMapping("user/link")
    @Operation(description = "项目关联用户")
    public Result linkUser(@RequestBody ProjectUserLinkDTO projectUserLinkDTO) {
        log.info("项目关联用户:  {}", projectUserLinkDTO);
        return projectsService.linkUser(projectUserLinkDTO);
    }
}

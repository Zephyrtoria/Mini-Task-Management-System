package com.github.zephyrtoria.task_system.controller;

import com.github.zephyrtoria.task_system.domain.dto.*;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.service.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks")
@Slf4j
@Tag(name = "任务接口")
public class TasksController {

    @Resource
    private TasksService tasksService;

    @PostMapping("")
    @Operation(description = "创建任务")
    public Result create(@RequestBody TasksCreateDTO tasksCreateDTO) {
        log.info("创建任务: {}", tasksCreateDTO);
        return tasksService.create(tasksCreateDTO);
    }

    @DeleteMapping("")
    @Operation(description = "删除任务")
    public Result delete(@RequestBody TasksDeleteDTO tasksDeleteDTO) {
        log.info("删除任务: {}", tasksDeleteDTO);
        return tasksService.delete(tasksDeleteDTO);
    }

    @PutMapping("")
    @Operation(description = "修改任务内容")
    public Result modify(@RequestBody TasksUpdateDTO tasksUpdateDTO) {
        log.info("修改任务内容: {}", tasksUpdateDTO);
        return tasksService.modify(tasksUpdateDTO);
    }

    @PutMapping("status")
    @Operation(description = "修改任务状态")
    public Result setStatus(@RequestBody TasksSetStatusDTO tasksSetStatusDTO) {
        log.info("修改任务状态: {}", tasksSetStatusDTO);
        return tasksService.status(tasksSetStatusDTO);
    }

    @GetMapping("{id}")
    @Operation(description = "查看任务详情")
    public Result query(@PathVariable Long id) {
        log.info("查看任务详情: {}", id);
        return tasksService.queryById(id);
    }

    @GetMapping("")
    @Operation(description = "查看指定项目下的所有任务")
    public Result queryAll(TasksQueryDTO tasksQueryDTO) {
        log.info("查看指定项目下的所有任务:  {}", tasksQueryDTO);
        return tasksService.queryAll(tasksQueryDTO);
    }
}

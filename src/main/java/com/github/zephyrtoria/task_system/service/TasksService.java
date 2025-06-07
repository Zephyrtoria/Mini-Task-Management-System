package com.github.zephyrtoria.task_system.service;

import com.github.zephyrtoria.task_system.domain.Tasks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zephyrtoria.task_system.domain.dto.*;
import com.github.zephyrtoria.task_system.domain.result.Result;

/**
* @author 23240
* @description 针对表【tasks】的数据库操作Service
* @createDate 2025-06-07 16:08:50
*/
public interface TasksService extends IService<Tasks> {

    Result create(TasksCreateDTO tasksCreateDTO);

    Result delete(TasksDeleteDTO tasksDeleteDTO);

    Result modify(TasksUpdateDTO tasksUpdateDTO);

    Result status(TasksSetStatusDTO tasksSetStatusDTO);

    Result queryById(Long id);

    Result queryAll(TasksQueryDTO tasksQueryDTO);
}

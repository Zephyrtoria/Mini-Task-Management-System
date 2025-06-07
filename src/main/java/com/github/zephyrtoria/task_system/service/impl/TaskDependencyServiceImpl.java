package com.github.zephyrtoria.task_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.TaskDependency;
import com.github.zephyrtoria.task_system.service.TaskDependencyService;
import com.github.zephyrtoria.task_system.mapper.TaskDependencyMapper;
import org.springframework.stereotype.Service;

/**
* @author 23240
* @description 针对表【task_dependency】的数据库操作Service实现
* @createDate 2025-06-07 16:08:43
*/
@Service
public class TaskDependencyServiceImpl extends ServiceImpl<TaskDependencyMapper, TaskDependency>
    implements TaskDependencyService{

}





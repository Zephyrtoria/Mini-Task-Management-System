package com.github.zephyrtoria.task_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.Tasks;
import com.github.zephyrtoria.task_system.service.TasksService;
import com.github.zephyrtoria.task_system.mapper.TasksMapper;
import org.springframework.stereotype.Service;

/**
* @author 23240
* @description 针对表【tasks】的数据库操作Service实现
* @createDate 2025-06-07 16:08:50
*/
@Service
public class TasksServiceImpl extends ServiceImpl<TasksMapper, Tasks>
    implements TasksService{

}





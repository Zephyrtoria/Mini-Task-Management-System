package com.github.zephyrtoria.task_system.mapper;

import com.github.zephyrtoria.task_system.domain.Tasks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.zephyrtoria.task_system.domain.dto.TasksQueryDTO;

import java.util.List;

/**
* @author 23240
* @description 针对表【tasks】的数据库操作Mapper
* @createDate 2025-06-07 16:08:50
* @Entity com.github.zephyrtoria.task_system.domain.Tasks
*/
public interface TasksMapper extends BaseMapper<Tasks> {

    List<Tasks> queryAll(TasksQueryDTO tasksQueryDTO);
}





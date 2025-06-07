package com.github.zephyrtoria.task_system.mapper;

import com.github.zephyrtoria.task_system.domain.TaskDependency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 23240
* @description 针对表【task_dependency】的数据库操作Mapper
* @createDate 2025-06-07 16:08:43
* @Entity com.github.zephyrtoria.task_system.domain.TaskDependency
*/
public interface TaskDependencyMapper extends BaseMapper<TaskDependency> {

    void insertBatch(List<TaskDependency> list);
}





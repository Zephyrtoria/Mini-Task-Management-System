package com.github.zephyrtoria.task_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.Projects;
import com.github.zephyrtoria.task_system.service.ProjectsService;
import com.github.zephyrtoria.task_system.mapper.ProjectsMapper;
import org.springframework.stereotype.Service;

/**
* @author 23240
* @description 针对表【projects】的数据库操作Service实现
* @createDate 2025-06-07 16:08:25
*/
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects>
    implements ProjectsService{

}





package com.github.zephyrtoria.task_system.service;

import com.github.zephyrtoria.task_system.domain.Projects;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zephyrtoria.task_system.domain.dto.ProjectCreateDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUpdateDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUserLinkDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;

/**
* @author 23240
* @description 针对表【projects】的数据库操作Service
* @createDate 2025-06-07 16:08:25
*/
public interface ProjectsService extends IService<Projects> {

    Result insert(ProjectCreateDTO projectCreateDTO);

    Result modify(ProjectUpdateDTO projectUpdateDTO);

    Result delete(Long id);

    Result queryAll();

    Result queryById(Long id);

    Result queryUsers(Long id);

    Result linkUser(ProjectUserLinkDTO projectUserLinkDTO);
}

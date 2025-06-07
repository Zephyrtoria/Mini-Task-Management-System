package com.github.zephyrtoria.task_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.UserProject;
import com.github.zephyrtoria.task_system.service.UserProjectService;
import com.github.zephyrtoria.task_system.mapper.UserProjectMapper;
import org.springframework.stereotype.Service;

/**
* @author 23240
* @description 针对表【user_project】的数据库操作Service实现
* @createDate 2025-06-07 16:08:52
*/
@Service
public class UserProjectServiceImpl extends ServiceImpl<UserProjectMapper, UserProject>
    implements UserProjectService{

}





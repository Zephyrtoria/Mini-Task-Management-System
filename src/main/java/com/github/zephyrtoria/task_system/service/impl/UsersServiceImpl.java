package com.github.zephyrtoria.task_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.Users;
import com.github.zephyrtoria.task_system.service.UsersService;
import com.github.zephyrtoria.task_system.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 23240
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-06-07 16:08:55
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}





package com.github.zephyrtoria.task_system.mapper;

import com.github.zephyrtoria.task_system.domain.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 23240
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-06-07 16:08:55
* @Entity com.github.zephyrtoria.task_system.domain.Users
*/
public interface UsersMapper extends BaseMapper<Users> {

    List<Users> queryAllByProjectId(Long projectId);
}





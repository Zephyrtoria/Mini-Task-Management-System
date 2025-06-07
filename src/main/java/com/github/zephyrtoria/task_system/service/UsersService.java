package com.github.zephyrtoria.task_system.service;

import com.github.zephyrtoria.task_system.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.zephyrtoria.task_system.domain.dto.UserSigninDTO;
import com.github.zephyrtoria.task_system.domain.dto.UserSignupDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;
import jakarta.servlet.http.HttpSession;

/**
* @author 23240
* @description 针对表【users】的数据库操作Service
* @createDate 2025-06-07 16:08:55
*/
public interface UsersService extends IService<Users> {

    Result signup(UserSignupDTO userSignupDTO);

    Result signin(UserSigninDTO userSigninDTO);

    Result me();

    Result queryById(Long id);
}

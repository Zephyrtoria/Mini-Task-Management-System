package com.github.zephyrtoria.task_system.controller;

import com.github.zephyrtoria.task_system.domain.dto.UserSigninDTO;
import com.github.zephyrtoria.task_system.domain.dto.UserSignupDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@Slf4j
@Tag(name = "用户接口")
public class UsersController {
    @Resource
    private UsersService usersService;

    @PostMapping("register")
    @Operation(description = "用户注册")
    public Result signup(@RequestBody UserSignupDTO userSignupDTO) {
        return usersService.signup(userSignupDTO);
    }

    @PostMapping("login")
    @Operation(description = "用户登录")
    public Result signin(@RequestBody UserSigninDTO userSigninDTO) {
        return usersService.signin(userSigninDTO);
    }

    @GetMapping("me")
    @Operation(description = "用户信息查看（自己）")
    public Result me() {
        return usersService.me();
    }

    @GetMapping("{id}")
    @Operation(description = "用户信息查看（其他用户）")
    public Result query(@PathVariable Long id) {
        return usersService.queryById(id);
    }
}

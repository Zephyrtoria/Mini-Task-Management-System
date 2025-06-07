package com.github.zephyrtoria.task_system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.Users;
import com.github.zephyrtoria.task_system.domain.dto.UserSigninDTO;
import com.github.zephyrtoria.task_system.domain.dto.UserSignupDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.domain.vo.UserMeVO;
import com.github.zephyrtoria.task_system.domain.vo.UserQueryVO;
import com.github.zephyrtoria.task_system.service.UsersService;
import com.github.zephyrtoria.task_system.mapper.UsersMapper;
import com.github.zephyrtoria.task_system.utils.PasswordEncoder;
import com.github.zephyrtoria.task_system.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.github.zephyrtoria.task_system.consts.RedisConsts.LOGIN_USER_REDIS_PREFIX;
import static com.github.zephyrtoria.task_system.consts.RedisConsts.LOGIN_USER_REDIS_TTL;

/**
 * @author 23240
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-06-07 16:08:55
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public Result signup(UserSignupDTO userSignupDTO) {
        Users users = query().eq("name", userSignupDTO.getName()).one();
        if (users != null) {
            return Result.fail(400,"当前用户名已被占用");
        }

        users = new Users();
        BeanUtil.copyProperties(userSignupDTO, users);
        users.setPassword(PasswordEncoder.encode(users.getPassword()));
        save(users);
        return Result.ok();
    }

    @Override
    public Result signin(UserSigninDTO userSigninDTO) {
        Users users = query().eq("name", userSigninDTO.getName()).one();
        if (users == null) {
            return Result.fail(400,"用户不存在");
        }
        if (!PasswordEncoder.matches(users.getPassword(), userSigninDTO.getPassword())) {
            return Result.fail(400,"密码错误");
        }

        // 登录成功
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_REDIS_PREFIX + token, users.getId().toString(), LOGIN_USER_REDIS_TTL, TimeUnit.MINUTES);
        return Result.ok(token);
    }

    @Override
    public Result me() {
        Long userId = UserHolder.getUserId();
        Users users = query().eq("id", userId).one();
        if (users == null) {
            return Result.fail(400,"用户不存在");
        }
        UserMeVO userMeVO = new UserMeVO();
        BeanUtil.copyProperties(users, userMeVO);
        return Result.ok(userMeVO);
    }

    @Override
    public Result queryById(Long id) {
        Users users = query().eq("id", id).one();
        if (users == null) {
            return Result.fail(400,"用户不存在");
        }
        UserQueryVO userQueryVO = new UserQueryVO();
        BeanUtil.copyProperties(users, userQueryVO);
        return Result.ok(userQueryVO);
    }
}





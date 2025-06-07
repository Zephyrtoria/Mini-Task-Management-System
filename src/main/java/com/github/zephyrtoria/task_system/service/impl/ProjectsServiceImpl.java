package com.github.zephyrtoria.task_system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.consts.ProjectsConsts;
import com.github.zephyrtoria.task_system.domain.Projects;
import com.github.zephyrtoria.task_system.domain.UserProject;
import com.github.zephyrtoria.task_system.domain.Users;
import com.github.zephyrtoria.task_system.domain.dto.ProjectInsertDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUpdateDTO;
import com.github.zephyrtoria.task_system.domain.dto.ProjectUserLinkDTO;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.domain.vo.ProjectsQueryVO;
import com.github.zephyrtoria.task_system.domain.vo.UserQueryVO;
import com.github.zephyrtoria.task_system.mapper.ProjectsMapper;
import com.github.zephyrtoria.task_system.mapper.UserProjectMapper;
import com.github.zephyrtoria.task_system.mapper.UsersMapper;
import com.github.zephyrtoria.task_system.service.ProjectsService;
import com.github.zephyrtoria.task_system.utils.UserHolder;
import jakarta.annotation.Resource;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.github.zephyrtoria.task_system.consts.DatabaseConsts.*;
import static com.github.zephyrtoria.task_system.consts.ProjectsConsts.ADMIN;

/**
 * @author 23240
 * @description 针对表【projects】的数据库操作Service实现
 * @createDate 2025-06-07 16:08:25
 */
@Service
public class ProjectsServiceImpl extends ServiceImpl<ProjectsMapper, Projects>
        implements ProjectsService {

    @Resource
    private UserProjectMapper userProjectMapper;

    @Resource
    private UsersMapper usersMapper;

    @Override
    @Transactional
    public Result insert(ProjectInsertDTO projectInsertDTO) {
        Projects projects = new Projects();
        BeanUtil.copyProperties(projectInsertDTO, projects);

        save(projects);

        Long userId = UserHolder.getUserId();
        UserProject link = new UserProject();
        link.setProjectId(projects.getId());
        link.setUserId(userId);
        link.setLevel(ADMIN);

        userProjectMapper.insert(link);

        return Result.ok(projects);
    }

    @Override
    public Result modify(ProjectUpdateDTO projectUpdateDTO) {
        Projects projects = new Projects();
        BeanUtil.copyProperties(projectUpdateDTO, projects);
        saveOrUpdate(projects);
        return Result.ok(projects);
    }

    @Override
    @Transactional
    public Result delete(Long id) {
        // 删除关联
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(PROJECT_ID, id);
        userProjectMapper.delete(wrapper);

        // 删除项目
        Projects projects = new Projects();
        projects.setId(id);
        removeById(projects);

        return Result.ok();
    }

    @Override
    @Transactional
    public Result queryAll() {
        Long userId = UserHolder.getUserId();
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(USER_ID, userId);
        List<UserProject> list = userProjectMapper.selectList(wrapper);
        List<Long> ids = new ArrayList<>();
        list.forEach(each -> ids.add(each.getProjectId()));

        List<ProjectsQueryVO> projectsQueryVOList = new ArrayList<>();
        listByIds(ids).forEach(each -> {
            ProjectsQueryVO projectsQueryVO = new ProjectsQueryVO();
            BeanUtil.copyProperties(each, projectsQueryVO);
            projectsQueryVOList.add(projectsQueryVO);
        });
        return Result.ok(projectsQueryVOList);
    }

    @Override
    public Result queryById(Long id) {
        Projects projects = query().eq(ID, id).one();
        ProjectsQueryVO projectsQueryVO = new ProjectsQueryVO();
        BeanUtil.copyProperties(projects, projectsQueryVO);
        return Result.ok(projectsQueryVO);
    }

    @Override
    @Transactional
    public Result queryUsers(Long id) {
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(PROJECT_ID, id);
        List<UserProject> list = userProjectMapper.selectList(wrapper);
        List<Long> ids = new ArrayList<>();
        list.forEach(each -> ids.add(each.getUserId()));

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(ID, ids);
        List<Users> users = usersMapper.selectList(queryWrapper);
        List<UserQueryVO> userQueryVOList = new ArrayList<>();
        users.forEach(each -> {
            UserQueryVO userQueryVO = new UserQueryVO();
            BeanUtil.copyProperties(each, userQueryVO);
            userQueryVOList.add(userQueryVO);
        });
        return Result.ok(userQueryVOList);
    }

    @Override
    @Transactional
    public Result linkUser(ProjectUserLinkDTO projectUserLinkDTO) {
        // 检查权限，不能操作自身不在的项目
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        Long userId = UserHolder.getUserId();
        wrapper.eq(USER_ID, userId).eq(PROJECT_ID, projectUserLinkDTO.getProjectId());
        UserProject link = userProjectMapper.selectOne(wrapper);
        if (link == null || link.getLevel() != ADMIN) {
            return Result.fail(400, "无权限添加组员");
        } else if (userId.equals(projectUserLinkDTO.getUserId())) {
            return Result.fail(400, "不能操作自身权限");
        }

        // 添加关联
        UserProject userProject = new UserProject();
        BeanUtil.copyProperties(projectUserLinkDTO, userProject);
        wrapper = new QueryWrapper<>();
        wrapper.eq(USER_ID, projectUserLinkDTO.getUserId()).eq(PROJECT_ID, projectUserLinkDTO.getProjectId());

        if (userProjectMapper.selectCount(wrapper) > 0) {
            // 已经存在关联，则修改level
            userProjectMapper.update(userProject, wrapper);
            return Result.ok();
        }
        // 不存在关联，加入关联
        userProjectMapper.insert(userProject);
        return Result.ok();
    }
}





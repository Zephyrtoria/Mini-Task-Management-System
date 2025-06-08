package com.github.zephyrtoria.task_system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.domain.Projects;
import com.github.zephyrtoria.task_system.domain.UserProject;
import com.github.zephyrtoria.task_system.domain.Users;
import com.github.zephyrtoria.task_system.domain.dto.ProjectCreateDTO;
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

    /**
     * 创建新projects
     *
     * @param projectCreateDTO
     * @return
     */
    @Override
    @Transactional
    public Result insert(ProjectCreateDTO projectCreateDTO) {
        // 存储项目
        Projects projects = new Projects();
        BeanUtil.copyProperties(projectCreateDTO, projects);

        save(projects);

        // 添加用户项目关系
        Long userId = UserHolder.getUserId();
        UserProject link = new UserProject();
        link.setProjectId(projects.getId());
        link.setUserId(userId);
        link.setLevel(ADMIN);
        userProjectMapper.insert(link);

        return Result.ok(projects);
    }

    /**
     * 修改projects信息
     *
     * @param projectUpdateDTO
     * @return
     */
    @Override
    public Result modify(ProjectUpdateDTO projectUpdateDTO) {
        // 是否在该表中，且是否是管理员
        if (!checkAdmin(projectUpdateDTO.getId())) {
            return Result.NO_AUTH;
        }

        // 保存修改
        Projects projects = new Projects();
        BeanUtil.copyProperties(projectUpdateDTO, projects);
        saveOrUpdate(projects);
        return Result.ok(projects);
    }

    /**
     * 删除项目，需要由管理员权限
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Result delete(Long id) {
        // 是否在该表中，且是否是管理员
        if (!checkAdmin(id)) {
            return Result.NO_AUTH;
        }

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

    /**
     * 查询当前users所有的projects
     *
     * @return
     */
    @Override
    public Result queryAll() {
        // 根据当前登录用户id获取
        Long userId = UserHolder.getUserId();
//        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
//        wrapper.eq(USER_ID, userId);
//        List<UserProject> list = userProjectMapper.selectList(wrapper);
//        List<Long> ids = new ArrayList<>();
//        list.forEach(each -> ids.add(each.getProjectId()));

        // 优化为一次多表查询
        List<Projects> projectsList = baseMapper.queryAllByUserId(userId);

        List<ProjectsQueryVO> projectsQueryVOList = new ArrayList<>();
        projectsList.forEach(each -> {
            ProjectsQueryVO projectsQueryVO = new ProjectsQueryVO();
            BeanUtil.copyProperties(each, projectsQueryVO);
            projectsQueryVOList.add(projectsQueryVO);
        });

        return Result.ok(projectsQueryVOList);
    }

    /**
     * 根据project_id查询projects
     *
     * @param id project_id
     * @return VO对象
     */
    @Override
    public Result queryById(Long id) {
        Projects projects = query().eq(ID, id).one();
        ProjectsQueryVO projectsQueryVO = new ProjectsQueryVO();
        BeanUtil.copyProperties(projects, projectsQueryVO);

        return Result.ok(projectsQueryVO);
    }

    /**
     * 根据project_id查询projects对应的users
     *
     * @param id project_id
     * @return users集合
     */
    @Override
    public Result queryUsers(Long id) {
/*        // 从关系表中查询project_id对应的所有user
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(PROJECT_ID, id);
        List<UserProject> list = userProjectMapper.selectList(wrapper);
        List<Long> ids = new ArrayList<>();
        list.forEach(each -> ids.add(each.getUserId()));

        // 再从users表中查询详细数据，封装成VO类返回
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(ID, ids);
        List<Users> users = usersMapper.selectList(queryWrapper);*/

        // 优化为一次多表查询
        List<Users> usersList = usersMapper.queryAllByProjectId(id);

        List<UserQueryVO> userQueryVOList = new ArrayList<>();
        usersList.forEach(each -> {
            UserQueryVO userQueryVO = new UserQueryVO();
            BeanUtil.copyProperties(each, userQueryVO);
            userQueryVOList.add(userQueryVO);
        });
        return Result.ok(userQueryVOList);
    }

    /**
     * 创建users和projects之间的关联，或修改权限
     *
     * @param projectUserLinkDTO
     * @return
     */
    @Override
    @Transactional
    public Result linkUser(ProjectUserLinkDTO projectUserLinkDTO) {
        // 检查权限，不能操作自身不在的项目
        Long userId = UserHolder.getUserId();
        if (!checkAdmin(projectUserLinkDTO.getProjectId())) {
            // 是否在该表中，且是否是管理员
            return Result.NO_AUTH;
        } else if (userId.equals(projectUserLinkDTO.getUserId())) {
            // 不能操作自己的权限
            return Result.SELF_MANIPULATE;
        }

        // 添加关联
        UserProject userProject = new UserProject();
        BeanUtil.copyProperties(projectUserLinkDTO, userProject);
        // 先查询是否已经存在关联，此处将插入和更新结合到一起了
        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(USER_ID, projectUserLinkDTO.getUserId()).eq(PROJECT_ID, projectUserLinkDTO.getProjectId());

        if (userProjectMapper.selectCount(wrapper) > 0) {
            // 已经存在关联，则修改level
            userProjectMapper.update(userProject, wrapper);
        } else {
            // 不存在关联，加入关联
            userProjectMapper.insert(userProject);
        }
        return Result.ok(userProject);
    }

    /**
     * 检查权限
     *
     * @param projectId
     * @return
     */
    private boolean checkAdmin(Long projectId) {
        Long userId = UserHolder.getUserId();

        QueryWrapper<UserProject> wrapper = new QueryWrapper<>();
        wrapper.eq(USER_ID, userId).eq(PROJECT_ID, projectId);
        UserProject link = userProjectMapper.selectOne(wrapper);
        // 是否在该表中，且是否是管理员
        return link != null && link.getLevel() == ADMIN;
    }
}





package com.github.zephyrtoria.task_system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.zephyrtoria.task_system.consts.TasksConsts;
import com.github.zephyrtoria.task_system.domain.TaskDependency;
import com.github.zephyrtoria.task_system.domain.Tasks;
import com.github.zephyrtoria.task_system.domain.UserProject;
import com.github.zephyrtoria.task_system.domain.dto.*;
import com.github.zephyrtoria.task_system.domain.result.Result;
import com.github.zephyrtoria.task_system.domain.vo.TasksQueryVO;
import com.github.zephyrtoria.task_system.mapper.TaskDependencyMapper;
import com.github.zephyrtoria.task_system.mapper.UserProjectMapper;
import com.github.zephyrtoria.task_system.service.TasksService;
import com.github.zephyrtoria.task_system.mapper.TasksMapper;
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
 * @description 针对表【tasks】的数据库操作Service实现
 * @createDate 2025-06-07 16:08:50
 */
@Service
public class TasksServiceImpl extends ServiceImpl<TasksMapper, Tasks>
        implements TasksService {

    @Resource
    private UserProjectMapper userProjectMapper;
    @Resource
    private TaskDependencyMapper taskDependencyMapper;

    @Override
    @Transactional
    public Result create(TasksCreateDTO tasksCreateDTO) {
        // 检查权限
        if (!checkAdmin(tasksCreateDTO.getProjectId())) {
            return Result.fail(400, "无权限添加任务");
        }

        // 添加任务
        Tasks tasks = new Tasks();
        BeanUtil.copyProperties(tasksCreateDTO, tasks);
        save(tasks);

        // 添加任务依赖
        List<Long> prevIds = insertDependency(tasks.getId(), tasksCreateDTO.getPrevTasksIds());

        // 返回VO类
        TasksQueryVO tasksQueryVO = new TasksQueryVO();
        BeanUtil.copyProperties(tasks, tasksQueryVO);
        tasksQueryVO.setPrevTasksIds(prevIds);
        return Result.ok(tasksQueryVO);
    }

    @Override
    @Transactional
    public Result delete(TasksDeleteDTO tasksDeleteDTO) {
        // 检查权限
        if (!checkAdmin(tasksDeleteDTO.getProjectId())) {
            return Result.fail(400, "无权限删除任务");
        }

        // 删除任务
        QueryWrapper<Tasks> wrapper = new QueryWrapper<>();
        Long taskId = tasksDeleteDTO.getId();
        wrapper.eq(ID, taskId);
        remove(wrapper);

        // 删除依赖任务，注意无论自己是prev还是next都要删除
        QueryWrapper<TaskDependency> dependencyQueryWrapper = new QueryWrapper<>();
        dependencyQueryWrapper.eq(NEXT_ID, taskId).or().eq(PREV_ID, taskId);
        taskDependencyMapper.delete(dependencyQueryWrapper);

        return Result.ok();
    }

    @Override
    @Transactional
    public Result modify(TasksUpdateDTO tasksUpdateDTO) {
        // 检查权限
        if (!checkAdmin(tasksUpdateDTO.getProjectId())) {
            return Result.fail(400, "无权限修改任务");
        }

        // 更新任务
        Tasks tasks = new Tasks();
        BeanUtil.copyProperties(tasksUpdateDTO, tasks);

        QueryWrapper<Tasks> wrapper = new QueryWrapper<>();
        Long taskId = tasksUpdateDTO.getId();
        wrapper.eq(ID, taskId);
        update(tasks, wrapper);

        // 删除依赖前缀任务
        QueryWrapper<TaskDependency> dependencyQueryWrapper = new QueryWrapper<>();
        dependencyQueryWrapper.eq(NEXT_ID, taskId);
        taskDependencyMapper.delete(dependencyQueryWrapper);

        // 添加新的依赖任务
        List<Long> prevIds = insertDependency(taskId, tasksUpdateDTO.getPrevTasksIds());

        // 返回VO类
        TasksQueryVO tasksQueryVO = new TasksQueryVO();
        BeanUtil.copyProperties(tasks, tasksQueryVO);
        tasksQueryVO.setPrevTasksIds(prevIds);
        return Result.ok(tasksQueryVO);
    }


    @Override
    @Transactional
    public Result status(TasksSetStatusDTO tasksSetStatusDTO) {
        // 检查权限
        if (!checkAdmin(tasksSetStatusDTO.getProjectId())) {
            return Result.fail(400, "无权限更改任务状态");
        }

        // 查询任务
        Long tasksId = tasksSetStatusDTO.getId();
        Tasks tasks = query().eq(ID, tasksId).one();
        if (tasks == null) {
            return Result.fail(400, "任务不存在");
        }

        // 查询前置任务是否完成
        QueryWrapper<TaskDependency> wrapper = new QueryWrapper<>();
        wrapper.eq(NEXT_ID, tasksId);
        List<TaskDependency> dependencies = taskDependencyMapper.selectList(wrapper);
        // 遍历
        for (TaskDependency dependency : dependencies) {
            Tasks prevTasks = query().eq(ID, dependency.getPrevId()).one();
            if (!prevTasks.getStatus().equals(TasksConsts.DONE)) {
                // 未完成则拒绝修改
                return Result.fail(400, "前置任务未完成");
            }
        }

        // 全部前置任务已完成，修改状态
        tasks.setStatus(tasksSetStatusDTO.getStatus());
        QueryWrapper<Tasks> taskWrapper = new QueryWrapper<>();
        taskWrapper.eq(ID, tasksId);
        update(tasks, taskWrapper);

        return Result.ok(tasks);
    }

    @Override
    public Result queryById(Long id) {
        // 查询任务
        Tasks tasks = query().eq(ID, id).one();

        // 查询依赖任务
        QueryWrapper<TaskDependency> wrapper = new QueryWrapper<>();
        wrapper.eq(NEXT_ID, id);
        List<TaskDependency> dependencies = taskDependencyMapper.selectList(wrapper);
        List<Long> prevIds = new ArrayList<>();
        dependencies.forEach(d -> prevIds.add(d.getPrevId()));

        // 封装为VO类返回
        TasksQueryVO tasksQueryVO = new TasksQueryVO();
        BeanUtil.copyProperties(tasks, tasksQueryVO);
        tasksQueryVO.setPrevTasksIds(prevIds);

        return Result.ok(tasksQueryVO);
    }

    @Override
    public Result queryAll(TasksQueryDTO tasksQueryDTO) {
        // 根据type指定排序方式
        List<Tasks> list = baseMapper.queryAll(tasksQueryDTO);
        return Result.ok(list);
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

    /**
     * 批量插入任务依赖项
     *
     * @param nextId
     * @param prevIds
     * @return
     */
    private List<Long> insertDependency(Long nextId, List<Long> prevIds) {
        if (prevIds == null || prevIds.isEmpty()) {
            return null;
        }
        List<TaskDependency> list = new ArrayList<>();
        prevIds.forEach(prevId -> {
            TaskDependency taskDependency = new TaskDependency();
            taskDependency.setNextId(nextId);
            taskDependency.setPrevId(prevId);
            list.add(taskDependency);
        });
        taskDependencyMapper.insertBatch(list);
        return prevIds;
    }
}





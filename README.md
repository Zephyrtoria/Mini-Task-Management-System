# 开发思路记录

# 需求拆分

## 需求描述

> 设计并实现一个迷你版的任务管理系统（类似于 Trello 或 Jira 的核心功能）。系统需要支持以下功能：
>
> 1. 用户可以创建多个项目（Project）。
> 2. 每个项目可以包含多个任务（Task）。
> 3. 每个任务可以设置状态（如：待办、进行中、已完成）。
> 4. 每个任务可以设置优先级（如：高、中、低）。
> 5. 用户可以对任务进行操作：
>
>    1. 创建任务。
>    2. 更新任务状态或优先级。
>    3. 删除任务。
> 6. 用户可以查看某个项目下的所有任务，并按状态或优先级进行筛选。

## 术语定义

|术语|说明|
| ------------| --------------------------------------------------------------------------------------|
|项目 Project|用户创建的任务集合，用于组织任务|
|任务 Task|属于某一项目，工作项，可设置状态和优先级|
|状态|属于某一任务，表示任务当前阶段，如“待办”、“进行中”、“已完成”、“前置任务未完成”|
|优先级|属于某一任务，表示任务紧急程度，如“高”、“中”、“低”|

## 需求拆分

### 用户模块

1. 用户注册
2. 用户登录：返回 token

   1. 使用拦截器进行 token 校验，之后使用 ThreadLocal 获取用户 Id
3. 用户基本信息查看、修改

### 项目模块

1. 创建项目
2. 查看项目
3. 邀请其他用户加入项目：关系表
4. 用户权限管理：可读可写（管理员，创建者默认为管理员权限）、可读不可写（普通成员）

### 任务模块

1. 创建任务：任务包含名称、状态、优先级、创建时间，需要指定项目，验证用户权限
2. 更新任务：验证权限，处理缓存
3. 删除任务：验证权限，处理缓存
4. 筛选任务：使用 Redis 缓存查询后的结果
5. 创建任务之间的依赖关系（存边，建图），可以使用拓扑排序建立任务流程图
6. 删除任务之间的依赖关系

# 数据库设计思路

## 用户表

1. 用户基本属性，现只设置 name 字段，同时用于登录和展示
2. 密码
3. 可扩展的其他描述信息（gender、age 等）

```sql
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(15) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);
```

## 项目表

1. 项目的基本属性，现只设置 name 字段
2. 可扩展其他属性

```sql
CREATE TABLE projects
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);
```

## 用户项目关系表

1. 由于用户和项目为多对多关系，创建中间表进行关联，同时存储用户在对应项目中的权限
2. 并且创建外键约束和 (user_id, project_id) 的主键联合索引

```sql
CREATE TABLE user_project
(
    user_id    BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    level      INT    NOT NULL,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
);
```

## 任务依赖表

1. 使用图结构存储任务的依赖关系，在数据库中存入前置任务和后缀任务的边
2. 可以方便修改任务状态时检查前置任务完成情况
3. 也可以通过拓扑排序画出流程图

```sql
CREATE TABLE task_dependency
(
    prev_id BIGINT NOT NULL,
    next_id BIGINT NOT NULL,

    FOREIGN KEY (prev_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (next_id) REFERENCES tasks (id) ON DELETE CASCADE
);
```

## 任务表

1. 需要有外键和项目表进行关联
2. 存储基本的属性：标题、详细内容、状态、优先级、创建时间

   1. 由于需求中提到需要根据状态、优先级、创建时间等字段进行查询和排序，考虑对其建立二级索引以减少查询开销
   2. 因为常见情况是根据单字段查询，所以建立单字段索引而不是联合索引

```sql
CREATE TABLE tasks
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id   BIGINT       NOT NULL,
    title        VARCHAR(100) NOT NULL,
    content      VARCHAR(1000),
    status       INT          NOT NULL DEFAULT 0,
    priority     INT          NOT NULL DEFAULT 1,
    created_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects (id),
    -- 单字段索引
    INDEX idx_tasks_status (status),
    INDEX idx_tasks_priority (priority),
    INDEX idx_tasks_created_time (created_time)
);
```

# API 设计思路

根据 RESTful API 的约定设计接口：

1. 查询 Get
2. 新增 Post
3. 修改 Put
4. 删除 Delete

API 使用 Postman 进行测试和导出

## 用户模块

1. 用户注册：需要新增用户，且可能携带较多数据（待扩展数据），使用 Post 请求
2. 用户登录：只需要用户 name 和 password，但是登录功能要求高安全性，使用 Post 请求体传输。登录后将在请求头中添加 JWT 信息，以便拦截器生效和便于查询
3. 用户信息查看（看自己）：直接使用 Get 请求，不需要携带参数，通过解析请求头中的 JWT 信息即可获取当前登录用户
4. 用户信息查看（看其他用户）：Get 请求，并通过路径参数携带要查询的用户，需要去除敏感信息

## 项目模块

1. 创建项目：插入新的项目，适用 Post 请求，通过回显的 project_id 和解析获取的 user_id 插入关系表，设置初始权限（level）为管理员
2. 修改项目：Put 请求，需要先查询关系表得到用户权限，再选择是否进行操作
3. 删除项目：Delete 请求，同上
4. 查看项目：Get 请求，此方法为查看当前用户所在的所有项目，可以考虑分页扩展
5. 查看当前项目信息：Get
6. 项目关联用户：Post 请求，需要用户拥有**当前项目的管理员权限**才能添加，即在关系表中插入另一用户 id 和指定项目 id
7. 查看项目关联用户：Get，需要权限

## 任务模块

1. 创建任务：Post 请求，需要权限，可以设定前置任务
2. 删除任务：Delete 请求，需要权限
3. 修改任务内容：Put 请求，需要权限，可先通过查看单个任务进行数据回显，方便修改。修改优先级也用同一接口，将修改状态单列一个接口
4. 修改任务状态（status）：Put 请求，需要权限，且修改前要查询前置任务已完成，否则返回错误码
5. 查看单个任务详情：Get
6. 查看所有任务：Get 请求，可传入任务状态（status）、任务优先级（priority）进行筛选或排序，并使用 type 字段指定排序类型，实现不排序、按优先级排序、按创建时间排序（使用 MyBatis XML 实现）

# 代码实现关键点

## 用户对项目操作的权限校验

```java
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
```

## 用户关联项目

```java
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
            return Result.fail(400, "无权限添加组员");
        } else if (userId.equals(projectUserLinkDTO.getUserId())) {
            // 不能操作自己的权限
            return Result.fail(400, "不能操作自身权限");
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
```

## 修改任务状态，校验前置任务完成情况

```java
        // 查询前置任务是否完成
        QueryWrapper<TaskDependency> wrapper = new QueryWrapper<>();
        wrapper.eq(NEXT_ID, tasksId);
        List<TaskDependency> dependencies = taskDependencyMapper.selectList(wrapper);
        // 遍历
        for (TaskDependency dependency : dependencies) {
            Tasks prevTasks = query().eq(ID, dependency.getPrevId()).one();
            if (!prevTasks.getStatus().equals(TasksConsts.DONE)) {
                // 未完成则拒绝修改
                return Result.PREV_TASK_UNDO;
            }
        }
```

## 根据传入值动态查询所有任务、动态排序

```xml
    <select id="queryAll" resultType="com.github.zephyrtoria.task_system.domain.Tasks">
        select * from tasks
        <where>
            project_id = ${projectId}
            <if test="status != null and status != 0">
                and status = ${status}
            </if>
            <if test="priority != null and priority != 0">
                and priority = ${priority}
            </if>
        </where>
        <choose>
            <when test="type == 1">
                order by priority desc
            </when>
            <when test="type == 2">
                order by priority
            </when>
            <when test="type == 3">
                order by created_time
            </when>
            <when test="type == 4">
                order by created_time desc
            </when>
        </choose>
    </select>
```

## Redis 缓存处理

使用 SpringCache 相关注解实现，对于增删改操作，会删除对应项目下的缓存

```java
    private void cleanCache(Long projectId) {
        log.info("删除缓存: {}", projectId);
        Set<String> keys = stringRedisTemplate.keys("tasksCache::projectId:" + projectId + ":*");
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    @GetMapping("")
    @Operation(description = "查看指定项目下的所有任务")
    @Cacheable(cacheNames = "tasksCache", key = "'projectId:'+#tasksQueryDTO.projectId+':type:'+#tasksQueryDTO.type+':status:'+#tasksQueryDTO.status+':priority:'+#tasksQueryDTO.priority")
    public Result queryAll(TasksQueryDTO tasksQueryDTO) {
        log.info("查看指定项目下的所有任务:  {}", tasksQueryDTO);
        return tasksService.queryAll(tasksQueryDTO);
    }
```

‍

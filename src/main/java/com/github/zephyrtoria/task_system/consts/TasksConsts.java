package com.github.zephyrtoria.task_system.consts;

public class TasksConsts {
    // 状态管理
    public static final int TODO = 1;
    public static final int DOING = 2;
    public static final int DONE = 3;

    // 优先级管理
    public static final int LOW = 1;
    public static final int MIDDLE = 2;
    public static final int HIGH = 3;

    // 排序方式指定
    public static final int BY_PRIORITY_HIGH2LOW = 1;
    public static final int BY_PRIORITY_LOW2HIGH = 2;
    public static final int BY_CREATE_TIME = 3;
    public static final int BY_CREATE_TIME_DESC = 4;
}

package com.github.zephyrtoria.task_system.consts;

public class TasksConsts {
    // 状态管理
    public static final int TODO = 0;
    public static final int DOING = 1;
    public static final int DONE = 2;
    public static final int WAITING_OTHER = 3;

    // 优先级管理
    public static final int LOW = 0;
    public static final int MIDDLE = 1;
    public static final int HIGH = 2;

    // 排序方式指定
    public static final int BY_PRIORITY_HIGH2LOW = 0;
    public static final int BY_PRIORITY_LOW2HIGH = 1;
    public static final int BY_CREATE_TIME = 2;
    public static final int BY_CREATE_TIME_DESC = 3;
}

package com.github.zephyrtoria.task_system.domain.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result LOGIN_FAILED = Result.fail(1001, "登录失败");
    public static Result PASSWORD_UNMATCH = Result.fail(1002, "密码错误");
    public static Result USER_NOT_EXIST = Result.fail(1003, "用户不存在");
    public static Result USER_IS_EXIST = Result.fail(1004, "用户名重复");
    public static Result NO_AUTH = Result.fail(2001, "用户无权限");
    public static Result SELF_MANIPULATE = Result.fail(2002, "用户不能操作自身权限");
    public static Result PREV_TASK_UNDO = Result.fail(3001, "前置任务未完成");

    public static Result ok() {
        return new Result(200, "success", null);
    }

    public static Result ok(Object data) {
        return new Result(200, "success", data);
    }

    public static Result fail(Integer code, String errorMsg) {
        return new Result(code, errorMsg, null);
    }
}

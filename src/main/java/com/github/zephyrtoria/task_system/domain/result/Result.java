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

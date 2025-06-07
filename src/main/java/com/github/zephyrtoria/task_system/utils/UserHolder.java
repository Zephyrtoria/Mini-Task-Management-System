package com.github.zephyrtoria.task_system.utils;

public class UserHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void saveUserId(Long userId){
        tl.set(userId);
    }

    public static Long getUserId(){
        return tl.get();
    }

    public static void removeUserId(){
        tl.remove();
    }
}

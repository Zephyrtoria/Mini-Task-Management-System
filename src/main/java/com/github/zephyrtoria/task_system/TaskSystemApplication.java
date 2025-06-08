package com.github.zephyrtoria.task_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.github.zephyrtoria.task_system.mapper")
@EnableCaching
public class TaskSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSystemApplication.class, args);
    }

}

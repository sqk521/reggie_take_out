package com.itheima.config.SpringMvcConfig.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/*
* 全局异常处理*/
@ControllerAdvice(annotations = {RestController.class, Controller.class})//通知拦截那些
@ResponseBody//返回json数据
@Slf4j
public class GlobalExceptionHandler {
    /*
     * 进行异常处理方法*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//表示处理这种异常，就是SQL异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)//表示处理这种异常，就是SQL异常
    public R<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}

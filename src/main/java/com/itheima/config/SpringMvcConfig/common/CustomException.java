package com.itheima.config.SpringMvcConfig.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
/*封装我们的自定义异常*/
public class CustomException extends RuntimeException{
    public CustomException(String msg){
        super(msg);
    }
}

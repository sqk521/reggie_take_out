package com.itheima.config.SpringMvcConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j//输出日志，方便调试
@ServletComponentScan//
@SpringBootApplication//启动类添加注解，用于过滤器
@EnableTransactionManagement//开启事务注解支持，多张表的操作要开启@Transactional事务控制
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功。。。");
    }
}

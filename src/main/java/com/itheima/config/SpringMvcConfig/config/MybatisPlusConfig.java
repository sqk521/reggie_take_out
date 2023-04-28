package com.itheima.config.SpringMvcConfig.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* 配置mybatisplus的分页插件*/
@Configuration//配置类,要加注解
public class MybatisPlusConfig {

    @Bean//加上bean注解，让spring管理它
    public MybatisPlusInterceptor mybatisPlusInterceptor(){//拦截器MybatisPlusInterceptor
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();//创建拦截器对象
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());//加入拦截器插件
        return mybatisPlusInterceptor;//返回
    }

}

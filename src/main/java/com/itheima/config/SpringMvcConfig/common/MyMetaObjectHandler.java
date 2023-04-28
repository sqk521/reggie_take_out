package com.itheima.config.SpringMvcConfig.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component//注解，让spring框架管理
@Slf4j//日志
public class MyMetaObjectHandler implements MetaObjectHandler {

    /*插入操作自动填充
    *实现接口之后，重写两个方法，一个是插入时填充，一个是修改时填充
    关于字段填充方式，使用metaObject的setValue来实现
    关于id的获取，我们之前是存到session里的，但在MyMetaObjectHandler类中不能获得HttpSession对象，
    * 所以我们需要用其他方式来获取登录用户Id */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充(insert)...");
        log.info(metaObject.toString());//此时字段都为空，下面需要填充对应的数值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //设置创建人id
        metaObject.setValue("createUser", BaseContext.getCurrentId());//获取值
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
    /*更新时候操作自动填充
     * */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充(update)...");
        log.info(metaObject.toString());//此时字段都为空，下面需要填充对应的数值
        metaObject.setValue("updateTime", LocalDateTime.now());
        //设置更新人id
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}

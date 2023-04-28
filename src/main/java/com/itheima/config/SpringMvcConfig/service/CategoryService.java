package com.itheima.config.SpringMvcConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.config.SpringMvcConfig.entity.Category;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;

//服务接口，继承IService，传入实体类
public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}

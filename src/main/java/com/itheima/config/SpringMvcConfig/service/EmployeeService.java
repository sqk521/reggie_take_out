package com.itheima.config.SpringMvcConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.config.SpringMvcConfig.entity.Employee;

//服务接口，继承IService，传入实体类
public interface EmployeeService extends IService<Employee> {
}

package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.mapper.EmployeeMapper;
import com.itheima.config.SpringMvcConfig.service.EmployeeService;
import com.itheima.config.SpringMvcConfig.entity.Employee;
import org.springframework.stereotype.Service;


/*
* 继承ServiceImpl，传入EmployeeMapper, Employee实体类，实现接口EmployeeService
* */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

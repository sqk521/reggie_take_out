package com.itheima.config.SpringMvcConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.config.SpringMvcConfig.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/*
* 创建Mapper接口，继承了BaseMapper，泛型为创建的实体类，
* 所继承的BaseMapper方法里面有各种方法，增删改查，功能非常强大*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

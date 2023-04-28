package com.itheima.config.SpringMvcConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.config.SpringMvcConfig.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

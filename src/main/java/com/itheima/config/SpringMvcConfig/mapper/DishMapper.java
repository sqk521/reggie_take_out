package com.itheima.config.SpringMvcConfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.config.SpringMvcConfig.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}

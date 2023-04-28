package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.entity.SetmealDish;
import com.itheima.config.SpringMvcConfig.mapper.SetmealDishMapper;
import com.itheima.config.SpringMvcConfig.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}

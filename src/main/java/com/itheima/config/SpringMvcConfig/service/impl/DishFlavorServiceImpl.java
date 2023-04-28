package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.entity.DishFlavor;
import com.itheima.config.SpringMvcConfig.mapper.DishFlavorMapper;
import com.itheima.config.SpringMvcConfig.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

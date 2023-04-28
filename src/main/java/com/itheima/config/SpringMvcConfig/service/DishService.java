package com.itheima.config.SpringMvcConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.config.SpringMvcConfig.dto.DishDto;
import com.itheima.config.SpringMvcConfig.entity.Dish;
import org.springframework.stereotype.Service;


public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味
    public void updateWithFlavor(DishDto dishDto);

}

package com.itheima.config.SpringMvcConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.config.SpringMvcConfig.dto.SetmealDto;
import com.itheima.config.SpringMvcConfig.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    //该方法的作用是将套餐的基本信息和套餐关联的菜品信息一块保存
    void saveWithDish(SetmealDto setmealDto);

    //删除套餐
    void deleteWithDish(List<Long> ids);
}

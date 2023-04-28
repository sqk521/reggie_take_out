package com.itheima.config.SpringMvcConfig.dto;

import com.itheima.config.SpringMvcConfig.entity.Setmeal;
import com.itheima.config.SpringMvcConfig.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    //扩展两个属性
    private List<SetmealDish> setmealDishes;
    private String categoryName;

}

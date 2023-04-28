package com.itheima.config.SpringMvcConfig.dto;

import com.itheima.config.SpringMvcConfig.entity.Dish;
import com.itheima.config.SpringMvcConfig.entity.DishFlavor;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品口味
    private List<DishFlavor> flavors = new ArrayList<>();

    //菜品分类名称
    private String categoryName;

    private Integer copies;
}

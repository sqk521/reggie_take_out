package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.common.CustomException;
import com.itheima.config.SpringMvcConfig.entity.Category;
import com.itheima.config.SpringMvcConfig.entity.Dish;
import com.itheima.config.SpringMvcConfig.entity.Setmeal;
import com.itheima.config.SpringMvcConfig.mapper.CategoryMapper;
import com.itheima.config.SpringMvcConfig.service.CategoryService;
import com.itheima.config.SpringMvcConfig.service.DishService;
import com.itheima.config.SpringMvcConfig.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//我们需要在删除数据之前，根据id值，去Dish表和Setmeal表中查询是否关联了数据
//如果存在关联数据，则不能删除，并抛一个异常
//如果不存在关联数据（也就是查询到的数据条数为0），正常删除即可
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;//注入菜品的服务

    @Autowired
    SetmealService setmealService;//注入套餐的服务

    /*根据id删除分类，删除之前需要进行判断*/
    @Override
    public void remove(Long ids){

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count = dishService.count(dishLambdaQueryWrapper);

        //查看当前分类是否关联了菜品，如果已经关联，则抛出异常
        if(count > 0){//已经关联了菜品,抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查看当前分类是否关联了套餐，如果已经关联，则抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0 ){//已经关联了套餐,抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(ids);

    }
}

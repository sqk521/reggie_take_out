package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.dto.DishDto;
import com.itheima.config.SpringMvcConfig.entity.Dish;
import com.itheima.config.SpringMvcConfig.entity.DishFlavor;
import com.itheima.config.SpringMvcConfig.mapper.DishMapper;
import com.itheima.config.SpringMvcConfig.service.DishFlavorService;
import com.itheima.config.SpringMvcConfig.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    //新增菜品，同时向两张表插入数据，菜品表dish和口味表dish_flavor
    @Transactional//因为多张表的操作，加入事务控制，同时在启动类加上事务注解支持
    public void saveWithFlavor(DishDto dishDto){
        //保存基本信息到dish表格
        this.save(dishDto);

        Long dishId = dishDto.getId();//上面已经将数据封装在了dishDto里面了，因此此处获得id

        //菜品口味集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历集合,将获取到的dishId赋值给dishFlavor的dishId属性
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味到dish_flavor表，因此我们要注入dishFlavorService
        dishFlavorService.saveBatch(flavors);
    }


    //根据id查询菜品信息和口味信息

    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        //拷贝，将dish,拷贝到dishDao
        BeanUtils.copyProperties(dish,dishDto);

        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    //更新菜品信息，同时更新对应的口味
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品表
        this.updateById(dishDto);

        //更新口味表
        //先清理当前表对应的口味delete
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());//此处查询获得id
        dishFlavorService.remove(queryWrapper);//根据id清理原本的口味数据

        //然后再添加新的口味数据insert
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map(item ->{//遍历，拿出每一项，set一个dishID，dishID从dishDto拿到
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


}

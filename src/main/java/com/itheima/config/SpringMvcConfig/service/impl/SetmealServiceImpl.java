package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.common.CustomException;
import com.itheima.config.SpringMvcConfig.dto.SetmealDto;
import com.itheima.config.SpringMvcConfig.entity.Setmeal;
import com.itheima.config.SpringMvcConfig.entity.SetmealDish;
import com.itheima.config.SpringMvcConfig.mapper.SetmealMapper;
import com.itheima.config.SpringMvcConfig.service.SetmealDishService;
import com.itheima.config.SpringMvcConfig.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    protected SetmealDishService setmealDishService;
    //新增套餐，同时保存套餐信息和
    @Transactional//同时操作了两张表，因此在此处开启事务
    public void saveWithDish(SetmealDto setmealDto) {

        //保存套餐的基本信息，操作setmeal insert
        this.save(setmealDto);

        //保存套餐和菜品的关联信息，操作setmeal_dish  insert
        //SetmealDish集合这里只有dish菜品 的id 没有setmealdish套餐的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item ->{
            item.setSetmealId(setmealDto.getId());//取出每个元素，
            return item;
        })).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);//批量保存
    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        //先判断是否可以删除，即查看status 若为1则在售卖 ，不能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //select * from setmeal where id in (ids) and status = 1
        //因为ids可能不只是一个，因此，此处先查找出来
        queryWrapper.in(Setmeal::getId,ids);

        //等值查询，查询状态为1，同时要满足在售卖
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);//得出数量
        log.info("要删除的套餐数量为{}",count);
        if(count > 0){
            throw new CustomException("套餐正在售卖，请先停售然后再删除");
        }
        //所选套餐列表中没有套餐再售卖,直接删除,删除套餐中的数据setmeal
        this.removeByIds(ids);

        //继续删除,删除套餐关系表中的数据，setmealdish
        LambdaQueryWrapper<SetmealDish> setmealdishqw = new LambdaQueryWrapper<>();

        //条件类似于 delete from setmeal_dish where setmeal_id in (1,2,3)
        setmealdishqw.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(setmealdishqw);


    }
}

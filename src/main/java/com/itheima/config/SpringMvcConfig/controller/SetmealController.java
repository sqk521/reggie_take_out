package com.itheima.config.SpringMvcConfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.dto.SetmealDto;
import com.itheima.config.SpringMvcConfig.entity.Category;
import com.itheima.config.SpringMvcConfig.entity.Setmeal;
import com.itheima.config.SpringMvcConfig.entity.SetmealDish;
import com.itheima.config.SpringMvcConfig.service.CategoryService;
import com.itheima.config.SpringMvcConfig.service.SetmealDishService;
import com.itheima.config.SpringMvcConfig.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.util.locale.provider.LocaleServiceProviderPool;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    /*根据条件，保存新增套餐数据*/
    @PostMapping//因为提交过来的是json数据，故因此要加@RequestBody
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息为{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐添加成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        //改造返回值对象,SetmealDto里面有名称，里面扩展的有名称
        Page<SetmealDto> dtopage= new Page<>(page,pageSize);
        //
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件
        queryWrapper.like(name != null,Setmeal::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //实现对象copy,将pageInfo信息copy到pageInfo2，同时忽略records的信息，这个是多余的
        BeanUtils.copyProperties(pageInfo,dtopage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{

            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝，将item拷贝到dto里面
            BeanUtils.copyProperties(item,setmealDto);
            //分类的ID
            Long categoryId = item.getCategoryId();

            //根据分类ID拿到分类名称
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String name1 = category.getName();//拿到分类名称
                setmealDto.setCategoryName(name1);//此处只放了名称
            }
            return setmealDto;

        }).collect(Collectors.toList());//收集起来转化成集合

        dtopage.setRecords(list);
        //此处，pageInfo的对象Setmeal只存了分类id,我们要的是分类名称
        //因此要根据分类的id获取分类名称
        return R.success(dtopage);
    }


    /*删除套餐*/
    @DeleteMapping
    public R<String> delete ( @RequestParam  List<Long> ids){
        log.info("要删除的id{}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }
}

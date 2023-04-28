package com.itheima.config.SpringMvcConfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.dto.DishDto;
import com.itheima.config.SpringMvcConfig.entity.Category;
import com.itheima.config.SpringMvcConfig.entity.Dish;
import com.itheima.config.SpringMvcConfig.service.CategoryService;
import com.itheima.config.SpringMvcConfig.service.DishFlavorService;
import com.itheima.config.SpringMvcConfig.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;



    /*新增菜品*/
    @PostMapping
    public R<String> save(@RequestBody  DishDto dishDto){//前端提交过来的为json数据，要加@RequestBody注解
        log.info(dishDto.toString());
        //需要操作两张表，菜品表和口味表，因此在service接口里面扩展新方法
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /*菜品信息分页*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("page={},pageSzie={},name={}",page,pageSize,name);

        //1构建分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //2条件查询器构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //3添加过滤条件,name不为空的话，在Dish中获取名字，查询name
        queryWrapper.like(name != null,Dish::getName,name);

        //4添加排序条件,根据更新时间降序排列
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //4分页查询
        dishService.page(pageInfo,queryWrapper);
        //此处，返回的数据没有分类的名称，不做处理的话不能显示菜品名称，
        // dish对象没有菜品分类名称属性，但是有菜品分类id，
        // 根据这个菜品分类id，去菜品分类表中查询对应的菜品分类名称
        //把DishDto看做是Dish类的基础上，增加了一个categoryName属性，到时候返回DishDto
        //将查询出来的dish数据，赋给dishDto，然后在根据dish数据中的category_id，
        // 去菜品分类表中查询到category_name，将其赋给dishDto


        //对象拷贝,第三个参数代表除了records，拷贝其余参数
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();

        //基于records处理
        List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            //拷贝item到dishDto
            BeanUtils.copyProperties(item,dishDto);
            //获得分类的id并赋值给categoryId
            Long categoryId = item.getCategoryId();//分类ID

            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);//此处是分类名称的赋值
            }
            return dishDto;
        }).collect(Collectors.toList());//收集起来，转化为集合

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /*新增菜品,回显菜品信息，故先查询对应菜品信息
    * 根据id查询菜品信息和口味信息*/
    @GetMapping("/{id}")
    public R<DishDto> getByIdWithFlavor(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /*修改保存菜品*/
    @PutMapping
    public R<String> update(@RequestBody  DishDto dishDto){//前端提交过来的为json数据，要加@RequestBody注解
        log.info(dishDto.toString());
        //需要操作两张表，菜品表和口味表，因此在service接口里面扩展新方法
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /*根据条件查询，即显示分类下的菜品数据*/
    @GetMapping("/list")
    public  R<List<Dish>> get(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //根据categoryId查询
        queryWrapper.eq(dish.getCategoryId()!= null ,Dish::getCategoryId,dish.getCategoryId());
        //只查询状态为1的菜品，起售菜品
        queryWrapper.eq(Dish::getStatus,1);
        //简单排序,先按照分类排序，然后按照更新时间
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }

}

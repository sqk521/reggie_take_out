package com.itheima.config.SpringMvcConfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.entity.Category;
import com.itheima.config.SpringMvcConfig.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")

public class CategoryController {

    @Autowired
    private CategoryService categoryService;//把创建的service接口注入进来
//    页面发送ajax请求，将新增分类窗口输入的数据以json形式提交给服务端
//    服务端Controller接收页面提交的数据并调用Service将数据存储到数据库
//    Service调用Mapper操作数据库，保存数据

    //通过监测NetWork，点击新增菜品分类表单的确定按钮和新增套餐请求的服务端地址和提交的json数据结构相同，
    // 所以服务端只需要提供一个方法统一处理即可
    @PostMapping//只需要接收页面数据并保存至数据库
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success(category.getType() == 1 ? "添加菜品分类成功！" : "添加套餐分类成功！");
    }

    /*分页信息查询,返回Page对象，传入page,pageSize*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page={},pageSzie={}",page,pageSize);

        //1构建分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //2条件查询器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //3添加排序条件,相当于sql  orderby,按照分类排序
        queryWrapper.orderByDesc(Category::getSort);
        //4分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    //删除分类，下面为需求分析
    /*页面发送ajax请求，将参数(id)提交给服务端
    服务端Controller接收页面提交的数据，并调用Service删除数据
    Service调用Mapper操作数据库*/
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id为{}",ids);
        categoryService.remove(ids);//removeById是由mp提供的，此处，要扩展自己的方法
        return R.success("分类信息删除成功");
    }

    /*根据id修改分类信息*/
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息为：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /*根据条件查询分类数据，套餐分类，菜品分类*/
    @GetMapping("/list")
    public R<List<Category>> list(Category category){//返回列表集合\
       //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件，根据type查询,先判断保证type不为空，然后添加条件，用双冒号
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        //优先使用分类，升序排列，然后根据更新时间，降序排列
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        //执行查询，返回list集合
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }

}

package com.itheima.config.SpringMvcConfig.controller;

//import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.entity.Employee;
import com.itheima.config.SpringMvcConfig.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils ;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j//日志
@RestController//
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired//把创建的service接口注入进来
    private EmployeeService employeeService;
    //前端返回json格式，要用@RequestBody，然后传进去对象
    //通过request，获取当前用户登陆信息

//    @RequestBody 主要用于接收前端传递给后端的json字符串（请求体中的数据）
//    HttpServletRequest 作用：如果登录成功，将员工对应的id存到session一份，
//    这样想获取一份登录用户的信息就可以随时获取出来
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1将页面提交的代码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对密码进行加密

        //2根据用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3如果没有查询到则返回登陆失败结果
        if(emp == null){
            return R.error("登陆失败");
        }
        //4密码比对，如果不一致返回登陆失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }

        //5查看员工状态
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6登陆成功，将员工id存入Session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);//setAttribute只放了Id
    }

    //员工退出方法
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1清理Session中保存的员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());

        //设置初始密码，进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户啊id
        Long empId = (Long) request.getSession().getAttribute("employee");//getAttribute返回对象，转化为long

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /*
    * 员工信息分页查询
    * 页面发送ajax请求，将分页查询参数（page,pageSize,name）提交给服务
    * 服务端Controller接收页面提交的数据并调用Service查询数据
    Service调用Mapper操作数据库，查询分页数据
    Controller将查询到的分页数据响应给页面
    页面接收到分页数据并通过ElementUI的Table组件展示到页面上*/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //1构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //2构建条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //3添加过滤条件,StringUtils.isNotEmpty(name),当名字不为空的时候添加进去
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //4添加排序条件，相当于sql  orderby
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);//按更新时间排序
        //5执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /*根据id修改员工状态及信息，暂时只修改状态
    在员工管理列表页面，可以对某个员工账号进行启用或者禁用操作。
    账号禁用的员工不能登录系统，启用后的员工可以正常登录。
    需要注意，只有管理员（admin用户）可以对其他普通用户进行启用、
    禁用操作，所以普通用户登录系统后启用、禁用按钮不显示。
    管理员admin登录系统可以对所有员工账号进行启用、禁用操作。
    如果某个员工账号状态为正常，则按钮显示为“禁用”，如果员工账号状态为已禁用，则按钮显示为“启用”
    页面发送ajax请求，将参数(id、status)提交到服务端
    服务端Controller接收页面提交的数据并调用Service更新数据
    Service调用Mapper操作数据库*/

    @PutMapping//在类上已经加上了/employee
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        log.info(employee.toString());//看能否接收到employee对象
        Long id = (Long)request.getSession().getAttribute("employee") ;
        //该id有问题是从分页查询拿过来的，点击禁用按钮发送给我们的id与数据库不一致
        employee.setUpdateUser(id);//那个用户进行了修改，上面通过session取出了id
        //js对数据处理丢失了精度，js处理的为前16位，解决方法将long转化为字符串
        employee.setUpdateTime(LocalDateTime.now());//更新时间
        employeeService.updateById(employee);//服务器更新员工
        return R.success("员工信息修改成功");
    }

    /*根据id查询员工信息*/
    @GetMapping("/{id}")//根据url地址传过来的，根据路径变量取过来
    public R<Employee> getById(@PathVariable  Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);//服务端查询id，返回对象
        if(employee != null){
            return R.success(employee);
        }
        return R.error("未查到该员工信息");
    };
}

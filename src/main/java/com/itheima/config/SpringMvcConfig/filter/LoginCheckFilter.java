package com.itheima.config.SpringMvcConfig.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.config.SpringMvcConfig.common.BaseContext;
import com.itheima.config.SpringMvcConfig.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
检查用户是否完成登陆
* */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")//   /*所有的请求都拦截
@Slf4j//日志
@ServletComponentScan//这样才会扫面Web的filter注解，从而创建过滤器
public class LoginCheckFilter implements Filter {//实现接口里面的方法

    //路径匹配器，支持通配符写法
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //过滤的方法
        HttpServletRequest request = (HttpServletRequest) servletRequest;//向下转型,这样就可以通过request调用getRequestURI()
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截到请求:{}",requestURI);
        //定义不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",//放行
                "/employee/logout",
                "/backend/**",//静态资源
                "/front/**",
                "/common/**",//
                //对用户登陆操作放行
                "/user/login",
                "/user/sendMsg"
        };
        //2判断本次请求是否需要处理
        boolean check = checkURI(urls, requestURI);

        //3如果不需要处理，则直接放行
        if(check){
            log.info("本次请求：{}不需要处理",requestURI);
            filterChain.doFilter(request,response);//放行
            return;
        }

        //4判断电脑端登陆状态，如果已经登陆直接放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已登陆，用户id为：{}",request.getSession().getAttribute("employee"));

            long id = Thread.currentThread().getId();
            log.info("线程id为：{}",id);
            //在此处获取线程id,根据session获取我们之前存的id的数值
            Long empId =(Long) request.getSession().getAttribute("employee");
            //使用BaseContext封装id
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);//放行
            return;
        }

        //判断移动端用户是否登录
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //5如果未登陆则返回未登陆结果，通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        log.info("用户id{}",request.getSession().getAttribute("employee"));
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    /*
    * 路径匹配方法，检查本次请求是否需要放行*/
    public boolean checkURI(String [] urls,String requestURI){
        for (String url:urls ) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}

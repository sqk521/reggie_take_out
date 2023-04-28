package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.entity.User;
import com.itheima.config.SpringMvcConfig.mapper.UserMapper;
import com.itheima.config.SpringMvcConfig.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

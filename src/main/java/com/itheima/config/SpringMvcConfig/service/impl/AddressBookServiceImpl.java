package com.itheima.config.SpringMvcConfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.SpringMvcConfig.entity.AddressBook;
import com.itheima.config.SpringMvcConfig.mapper.AddressBookMapper;
import com.itheima.config.SpringMvcConfig.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}

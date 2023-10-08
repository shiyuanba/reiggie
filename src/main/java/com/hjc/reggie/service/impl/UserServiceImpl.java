package com.hjc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.reggie.entity.User;
import com.hjc.reggie.mapper.UserMapper;
import com.hjc.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

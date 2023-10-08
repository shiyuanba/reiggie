package com.hjc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

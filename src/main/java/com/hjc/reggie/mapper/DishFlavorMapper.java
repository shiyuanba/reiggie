package com.hjc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}

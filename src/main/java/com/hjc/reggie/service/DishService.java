package com.hjc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.reggie.dto.DishDto;
import com.hjc.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据。
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);
}

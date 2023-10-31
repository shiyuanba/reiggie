package com.hjc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.reggie.dto.DishDto;
import com.hjc.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据。
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味信息。
    public void updateWithFlavor(DishDto dishDto);

    //修改菜品状态
    public boolean setStatus(Integer statu, List<Long> ids);

    //检查与菜品相关套餐状态
    public boolean checkStatus(List<Long> ids);
}
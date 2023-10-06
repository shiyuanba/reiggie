package com.hjc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.reggie.dto.SetmealDto;
import com.hjc.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
}

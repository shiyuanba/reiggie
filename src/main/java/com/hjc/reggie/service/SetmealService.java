package com.hjc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.reggie.dto.SetmealDto;
import com.hjc.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id查询套餐信息和菜品信息
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 删除套擦，同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}

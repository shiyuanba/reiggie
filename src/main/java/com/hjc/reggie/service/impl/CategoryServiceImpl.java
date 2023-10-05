package com.hjc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.reggie.common.CustomException;
import com.hjc.reggie.entity.Category;
import com.hjc.reggie.entity.Dish;
import com.hjc.reggie.entity.Setmeal;
import com.hjc.reggie.mapper.CategoryMapper;
import com.hjc.reggie.service.CategoryService;
import com.hjc.reggie.service.DishService;
import com.hjc.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联商品，如果关联，则抛出异常
        LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(queryWrapper1);
        if (dishCount > 0) {
            throw new CustomException("当前分类项关联菜品，不能删除");
        }


        //查询当前分类是否关联套餐，如果关联，则抛出异常
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId, id);
        int setMealCount = setmealService.count(queryWrapper2);
        if (setMealCount > 0) {
            throw new CustomException("当前分类项关联套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}

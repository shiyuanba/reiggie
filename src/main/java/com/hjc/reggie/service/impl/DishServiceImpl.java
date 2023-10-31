package com.hjc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.reggie.common.CustomException;
import com.hjc.reggie.dto.DishDto;
import com.hjc.reggie.entity.Dish;
import com.hjc.reggie.entity.DishFlavor;
import com.hjc.reggie.entity.Setmeal;
import com.hjc.reggie.entity.SetmealDish;
import com.hjc.reggie.mapper.DishMapper;
import com.hjc.reggie.service.DishFlavorService;
import com.hjc.reggie.service.DishService;
import com.hjc.reggie.service.SetmealDishService;
import com.hjc.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品,同时插入菜品对应的口味数据。
     *
     * @param dishDto
     */
    @Transactional      //开启事务
    public void saveWithFlavor(DishDto dishDto) {
        //保存基本的菜品信息到dish表中
        this.save(dishDto);
        //获取菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味到dish_flavor表中
        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();

        //从dish表中获取菜品信息
        Dish dish = this.getById(id);

        //从dish_flavor中获取菜品信息的所有口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(dish,dishDto);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息，同时更新口味信息。
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 修改菜品状态
     * @param statu
     * @param ids
     * @return
     */
    @Transactional
    public boolean setStatus(Integer statu,List<Long> ids){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);

        List<SetmealDish> setmealDishes = getSetmealDishes(ids);

        if (setmealDishes != null && statu == 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                Long setmealId = setmealDish.getSetmealId();

                Setmeal setmeal = setmealService.getById(setmealId);
                if (setmeal != null || setmeal.getStatus() != 0) {
                    throw new CustomException("与该菜品相关套餐正在售卖中...");//错误
                }
            }
        }

        List<Dish> list = this.list(queryWrapper);
        for (Dish dish : list) {
            LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.in(Dish::getId,dish.getId());
            if (dish.getStatus() == statu) {
                throw new CustomException("商品状态不一致！");
            }
            dish.setStatus(statu);
            this.update(dish,queryWrapper1);
        }
        return true;
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    public boolean checkStatus(List<Long> ids){
        List<SetmealDish> setmealDishes = getSetmealDishes(ids);
        if (setmealDishes != null) {
            for (SetmealDish setmealDish : setmealDishes) {
                Long setmealId = setmealDish.getSetmealId();
                Setmeal setmeal = setmealService.getById(setmealId);
                if (setmeal != null || setmeal.getStatus() != 0) {
                    throw new CustomException("与该菜品相关套餐正在售卖中...");//错误
                }
            }
        }
        return true;
    }

    /**
     * 返回SetmealDish对象，用于检查售卖状态。
     * @param ids
     * @return
     */
    public List<SetmealDish> getSetmealDishes(List<Long> ids){
        LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
        setmealDishWrapper.in(ids != null,SetmealDish::getDishId,ids);

        return setmealDishService.list(setmealDishWrapper);
    }
}

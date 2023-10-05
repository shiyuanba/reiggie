package com.hjc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.reggie.dto.DishDto;
import com.hjc.reggie.entity.Dish;
import com.hjc.reggie.entity.DishFlavor;
import com.hjc.reggie.mapper.DishMapper;
import com.hjc.reggie.service.DishFlavorService;
import com.hjc.reggie.service.DishService;
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
}

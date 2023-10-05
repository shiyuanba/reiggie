package com.hjc.reggie.controller;

import com.hjc.reggie.common.R;
import com.hjc.reggie.dto.DishDto;
import com.hjc.reggie.entity.Dish;
import com.hjc.reggie.entity.DishFlavor;
import com.hjc.reggie.service.DishFlavorService;
import com.hjc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.save(dishDto);
        return null;
    }

}

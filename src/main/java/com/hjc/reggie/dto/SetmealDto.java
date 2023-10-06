package com.hjc.reggie.dto;

import com.hjc.reggie.entity.Setmeal;
import com.hjc.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

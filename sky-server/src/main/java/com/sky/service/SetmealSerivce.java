package com.sky.service;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface SetmealSerivce {
    List<Setmeal> list(Integer categoryId);

    List<DishItemVO> dishList(Integer id);
}

package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void startOrStop(Integer status, Long id);

    void add(DishDTO dishDTO);

    void delete(List<Long> ids);

    DishVO getById(Long id);

    void update(DishDTO dishDTO);

    List<DishVO> getByCategoryId(Integer categoryId);
}

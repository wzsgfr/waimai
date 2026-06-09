package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealSerivce;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j

public class SetmealSerivceImpl implements SetmealSerivce {
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    @Cacheable(value = "setmealCache",key = "#categoryId")
    public List<Setmeal> list(Integer categoryId) {
        List<Setmeal> list=setmealMapper.list(categoryId);
        return list;
    }

    @Override
    @Cacheable(value = "setmealCache",key = "#id")
    public List<DishItemVO> dishList(Integer id) {
        List<Integer> ids=setmealMapper.dishIds(id);
        List<Dish> list=setmealMapper.dishList(ids);
        List<DishItemVO> dishItemVOList=new ArrayList<>();
        for (Dish dish : list){
            DishItemVO dishItemVO=new DishItemVO();
            BeanUtils.copyProperties(dish,dishItemVO);
            Integer copies = setmealMapper.getCopiesById(dish.getId());
            log.info("copies:{},{}",dish.getId(),copies);
            dishItemVO.setCopies(copies);
            dishItemVOList.add(dishItemVO);
        }
        return dishItemVOList;
    }
}

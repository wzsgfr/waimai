package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class DishServiceImpl  implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    @Transactional
    @CacheEvict(value = "dishCache",allEntries = true)
    public void startOrStop(Long status, Long id) {
        if (status == 1)
        {
            dishMapper.startOrStop(status,id);
        }
        else
        {
            Long setmealId= setmealDishMapper.setmealId(id);
            setmealMapper.status(0L,setmealId);
            dishMapper.startOrStop(status,id);
        }
    }
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Override
    @Transactional
    @CacheEvict(value = "dishCache",allEntries = true)
    public void add(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.add(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }

    }
    @Override
    @Transactional
    @CacheEvict(value = "dishCache",allEntries = true)
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.selectId(id);
            if(dish.getStatus() == 1)
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            int count = setmealDishMapper.selectid(id);
            if(count > 0) {
                log.info("菜品正在起售中");
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        dishMapper.delete(ids);
        dishFlavorMapper.deleteByDishId(ids);
    }

    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.selectId(id);
        BeanUtils.copyProperties(dish,dishVO);

        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "dishCache",allEntries = true)
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        dishFlavorMapper.delete(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
       if (flavors != null && flavors.size() > 0){
           for (DishFlavor flavor : flavors) {
               flavor.setDishId(dish.getId());
           }
           dishFlavorMapper.insertBatch(flavors);
       }



    }

    @Override
    @Cacheable(value = "dishCache",key = "#categoryId")
    public List<DishVO> getByCategoryId(Integer categoryId) {
        List<DishVO> dishVOList = new ArrayList<>();
        List<Dish> list = dishMapper.getByCategoryId(categoryId);
        for (Dish dish : list) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish,dishVO);
            dishVO.setFlavors(dishFlavorMapper.getByDishId(dish.getId()));
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}

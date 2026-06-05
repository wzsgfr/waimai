package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @Select("select count(1) from dish where category_id = #{id}")
    int countByCategoryId(Long id);
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);
}

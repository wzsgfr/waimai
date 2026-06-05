package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {
    @Select("SELECT COUNT( *) FROM setmeal_dish WHERE dish_id = #{id};")
    int selectid(Long id);
}

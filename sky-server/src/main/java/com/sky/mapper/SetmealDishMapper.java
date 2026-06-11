package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {
    @Select("SELECT COUNT( *) FROM setmeal_dish WHERE dish_id = #{id};")
    int selectid(Long id);
    @Select("select * from setmeal where id=#{setmealId}")
    Setmeal selectId(Long setmealId);
}

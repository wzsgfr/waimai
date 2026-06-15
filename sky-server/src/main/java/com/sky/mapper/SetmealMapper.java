package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> list(Integer categoryId);
    @Select("select dish_id from setmeal_dish where setmeal_id = #{id}")
    List<Integer> dishIds(Integer id);

    List<Dish> dishList(List<Integer> ids);
    @Select("select copies from setmeal_dish where dish_id = #{id}")
    Integer getCopiesById(Long id);

    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id}")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Setmeal getById(Integer id);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishesById(Integer id);
}


package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

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
    @Select("select * from setmeal where category_id = #{categoryId} and status=1")
    List<Setmeal> list(Integer categoryId);
    @Select("select dish_id from setmeal_dish where setmeal_id = #{id}")
    List<Integer> dishIds(Integer id);

    List<Dish> dishList(List<Integer> ids);
    @Select("select copies from setmeal_dish where dish_id = #{id}")
    Integer getCopiesById(Long id);

    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id} ")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    Setmeal getById(Integer id);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishesById(Integer id);
    @AutoFill(value = OperationType.INSERT)
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into setmeal (name,category_id,price,status,create_time,update_time,create_user,update_user,image,description) values (#{name},#{categoryId},#{price},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser},#{image},#{description})")
    void add(Setmeal setmeal);
    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set name = #{name},category_id = #{categoryId},price = #{price},status = #{status},update_time = #{updateTime},update_user = #{updateUser},image = #{image},description = #{description} where id = #{id}")
    void update(Setmeal setmeal);

    @Select("select status from dish where id=#{dishId} ")
    Integer startOrStop(Long dishId);
    @Update("update setmeal set status = #{status} where id = #{id}")
    void status(Long status, Long id);
    @Delete("delete from setmeal where id = #{id}")
    void delete(Long id);
}


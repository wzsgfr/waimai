package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select count(1) from dish where category_id = #{id}")
    int countByCategoryId(Long id);
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
    @AutoFill(value = OperationType.INSERT)

    @Options(useGeneratedKeys = true,keyProperty = "id")//注解返回id
    @Insert("insert into dish (name, category_id, price, status, create_time, update_time, create_user, update_user,image,description) values (#{name}, #{categoryId}, #{price}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser},#{image},#{description})")
    void add(Dish dish);
    @Select("select * from dish where id = #{id}")
    Dish selectId(Long id);

    void delete(List<Long> ids);

    @Select("select * from dish where category_id = #{categoryId} and status=1")
    @Options(useCache = true,keyProperty = "id")
    List<Dish> getByCategoryId(Integer categoryId);
    @Update("update dish set status = #{status} where id = #{id}")
    void startOrStop(Long status, Long id);
    @Select("select count(1) from dish where status = #{i}")
    Integer countByStatus(int i);
}

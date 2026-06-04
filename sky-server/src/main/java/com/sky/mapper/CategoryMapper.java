package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user,status) values (#{type}, #{name}, #{sort}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser},#{status})")
    void add(Category category);
    @Delete("delete from category where id = #{id}")
    void delete(Long id);
    List list(Integer type);
}

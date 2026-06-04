package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(Category category);
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user,status) values (#{type}, #{name}, #{sort}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser},#{status})")
    void add(Category category);
    @Delete("delete from category where id = #{id}")
    void delete(Long id);
}

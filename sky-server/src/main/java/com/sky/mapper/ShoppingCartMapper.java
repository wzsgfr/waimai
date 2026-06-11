package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    @Options(useCache = true,keyProperty = "id")
    List<ShoppingCart> SelectOne(ShoppingCart shoppingCart);
    @Update("update shopping_cart set number=number+1 where id=#{id}")
    void add(ShoppingCart shoppingCart);
    @Insert("insert into shopping_cart (user_id,dish_id,dish_flavor,setmeal_id,name,image,amount,create_time,number) values (#{userId},#{dishId},#{dishFlavor},#{setmealId},#{name},#{image},#{amount},#{createTime},#{number})")
    void addAll(ShoppingCart shoppingCart);

    List<ShoppingCart> selectAll(Long currentId);
    @Delete("delete from shopping_cart where user_id=#{currentId}")
    void delete(Long currentId);
    @Update("update shopping_cart set number=number-1 where id=#{id}")
    void minu(ShoppingCart cart);
    @Delete("delete from shopping_cart where id= #{id}")
    void deleteId(Long id);
}

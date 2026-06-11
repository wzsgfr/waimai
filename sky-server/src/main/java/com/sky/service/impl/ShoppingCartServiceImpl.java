package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j

public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}",shoppingCartDTO);
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        if(shoppingCart.getDishId() != null){
            Dish dish = dishMapper.selectId(shoppingCart.getDishId());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setName(dish.getName());
            shoppingCart.setAmount(dish.getPrice());
        }else {
            Setmeal setmeal=setmealDishMapper.selectId(shoppingCart.getSetmealId());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setAmount(setmeal.getPrice());
        }
        shoppingCart.setCreateTime(LocalDateTime.now());
        log.info("购物车数据：{}",shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.SelectOne(shoppingCart);
        if(list.size() > 0){
            shoppingCart=list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.add(shoppingCart);
        } else  {
            shoppingCart.setNumber(1);
            shoppingCartMapper.addAll(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {
        log.info("查询购物车");
        List<ShoppingCart> list = shoppingCartMapper.selectAll(BaseContext.getCurrentId());
        return list;
    }

    @Override
    public void clean() {
        shoppingCartMapper.delete(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.SelectOne(shoppingCart);
        if(list.size() > 0){
            ShoppingCart cart = list.get(0);
            if(cart.getNumber() == 1){
                shoppingCartMapper.deleteId(cart.getId());
            }
            else {
                cart.setNumber(cart.getNumber()-1);
                shoppingCartMapper.minu(cart);
            }
        }
    }
}

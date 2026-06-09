package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealSerivce;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
@Slf4j

public class SetmealController {
    @Autowired
    private SetmealSerivce setmealSerivce;
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Integer categoryId){
        log.info("查询分类id为{}的套餐数据",categoryId);
        List<Setmeal> list = setmealSerivce.list(categoryId);
        return Result.success(list);
    }
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dishList(@PathVariable Integer id){
        log.info("查询套餐id为{}的套餐数据",id);
        List<DishItemVO> list = setmealSerivce.dishList(id);
        return Result.success(list);
    }

}

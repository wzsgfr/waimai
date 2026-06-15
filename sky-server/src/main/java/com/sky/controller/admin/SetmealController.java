package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")

public class SetmealController {
    @Autowired
    private SetmealSerivce setmealSerivce;
    @GetMapping("/page")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealSerivce.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

}

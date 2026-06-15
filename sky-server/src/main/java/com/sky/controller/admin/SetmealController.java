package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealSerivce;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Integer id){
        log.info("查询id为{}的套餐",id);
        SetmealVO setmealVO = setmealSerivce.getById(id);
        return Result.success(setmealVO);
    }
    @PostMapping
    public Result add(@RequestBody SetmealDTO setmealDTO ){
        log.info("新增套餐：{}",setmealDTO);
        setmealSerivce.add(setmealDTO);
        return Result.success();
    }


}

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

import java.util.List;

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
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("编辑套餐：{}",setmealDTO);
        setmealSerivce.update(setmealDTO);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Long status,Long id){
        log.info("起售或停售套餐：{}",id);
        setmealSerivce.startOrStop(status,id);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}",ids);
        setmealSerivce.delete(ids);
        return Result.success();
    }

}

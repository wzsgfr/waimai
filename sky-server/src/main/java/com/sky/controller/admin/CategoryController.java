package com.sky.controller.admin;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/category")
@RestController
@Slf4j

public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("分类状态：{}",status);
        categoryService.startOrStop(status,id);
        return Result.success();
    }
    @PostMapping
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("分类信息：{}",categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("分类信息：{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(Long id){
        log.info("分类id：{}",id);
        categoryService.delete(id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        log.info("查询分类：{}",type);
        List<Category> list= categoryService.list(type);
        return Result.success(list);

    }
}

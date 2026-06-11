package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")

public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增地址");
        addressBookService.add(addressBook);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("查询地址");
        List<AddressBook> list =addressBookService.list();
        return Result.success(list);
    }
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        log.info("查询默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        log.info("修改地址");
        addressBookService.update(addressBook);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(Long id){
        log.info("删除地址");
        addressBookService.delete(id);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("查询地址");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址");
        addressBookService.updateDefault(addressBook);
        return Result.success();
    }
}

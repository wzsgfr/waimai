package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/conditionSearch")
    public Result conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("条件搜索订单：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    @GetMapping("/statistics")
    public Result statistics(){
        log.info("统计订单");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id){
        log.info("订单详情，订单id为{}", id);
        OrderVO orderVO = orderService.show(id);
        return Result.success(orderVO);
    }
}

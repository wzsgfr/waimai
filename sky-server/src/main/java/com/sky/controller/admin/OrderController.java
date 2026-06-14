package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/confirm")
    public Result confirm(@RequestBody  OrdersDTO ordersDTO   ){
        log.info("订单确认，订单id为{}", ordersDTO);
        Long id = ordersDTO.getId();
        orderService.confirm(Math.toIntExact(id));
        return Result.success();
    }
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO  ordersRejectionDTO){
        log.info("订单拒绝，订单id为{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO  ordersCancelDTO ){
        log.info("订单取消，订单id为{}",ordersCancelDTO );
        orderService. amindCancel(ordersCancelDTO);
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id){
        log.info("订单派送，订单id为{}", id);
        orderService.delivery(id);
        return Result.success();
    }
}

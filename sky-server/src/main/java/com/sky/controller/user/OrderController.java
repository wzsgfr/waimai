package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/order")

public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}",ordersSubmitDTO);
          OrderSubmitVO orderSubmitVO= orderService.submit(ordersSubmitDTO);
          return Result.success(orderSubmitVO);
    }
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }
    @GetMapping("/historyOrders")
    public Result history(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("查询历史订单：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.history(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> show(@PathVariable Long id){
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.show(id);
        return Result.success(orderVO);
    }
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id){
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

}

package com.sky.task;

import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderService orderService;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void processTimeoutOrder(){
        log.info("处理超时订单");
        orderService.processTimeoutOrder();
    }


}

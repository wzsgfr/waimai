package com.sky.service.impl;

import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service

public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private ReportService reportService;
    @Override
    public BusinessDataVO getBusinessData() {
        LocalDate time = LocalDate.now();
        OrderReportVO orderReportVO = reportService.orderStatistics(time, time);
        BusinessDataVO businessDataVO = new BusinessDataVO();
        businessDataVO.setValidOrderCount(orderReportVO.getValidOrderCount());
        businessDataVO.setOrderCompletionRate(orderReportVO.getOrderCompletionRate());
        UserReportVO userReportVO = reportService.userStatistics(time, time);
        businessDataVO.setNewUsers(Integer.valueOf(userReportVO.getNewUserList()));
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(time, time);
        businessDataVO.setTurnover(Double.valueOf(turnoverReportVO.getTurnoverList()));
        businessDataVO.setUnitPrice(businessDataVO.getTurnover() / businessDataVO.getValidOrderCount());
        return businessDataVO;
    }
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        SetmealOverViewVO setmealOverViewVO=new SetmealOverViewVO();
        setmealOverViewVO.setSold(setmealMapper.count(1));
        setmealOverViewVO.setDiscontinued(setmealMapper.count(0));
        return  setmealOverViewVO;
    }
    @Autowired
    private DishMapper dishMapper;
    @Override
    public DishOverViewVO overviewDishes() {
        DishOverViewVO dishOverViewVO=new DishOverViewVO();
        dishOverViewVO.setSold(dishMapper.countByStatus(1));
        dishOverViewVO.setDiscontinued(dishMapper.countByStatus(0));
        return dishOverViewVO;
    }
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public OrderOverViewVO overviewOrders() {
        OrderOverViewVO orderOverViewVO=new OrderOverViewVO();
        orderOverViewVO.setWaitingOrders(orderMapper.countByStatus(2));
        orderOverViewVO.setDeliveredOrders(orderMapper.countByStatus(3));
        orderOverViewVO.setCompletedOrders(orderMapper.countByStatus(5));
        orderOverViewVO.setCancelledOrders(orderMapper.countByStatus(6));
        orderOverViewVO.setAllOrders(orderMapper.count());
        return orderOverViewVO;
    }
}

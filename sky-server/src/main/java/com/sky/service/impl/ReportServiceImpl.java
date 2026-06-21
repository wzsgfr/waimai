package com.sky.service.impl;

import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = dateList(begin, end);
        List<Double> turnoverList = new ArrayList<>();
        String dateListString = StringUtils.join(dateList, ",");

        for (LocalDate date : dateList) {
            LocalDateTime maxTime = LocalDateTime.of(date, LocalTime.MAX);
            LocalDateTime minTime = LocalDateTime.of(date, LocalTime.MIN);
            Map map = new HashMap();
            map.put("begin",minTime);
            map.put("end",maxTime);
            map.put("status",5);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        String turnoverListString =StringUtils.join(turnoverList, ",");
        return new TurnoverReportVO(dateListString,turnoverListString);
    }


    @Autowired
    private UserMapper userMapper;
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<LocalDate> dateList = dateList(begin, end);
        String dateListString = StringUtils.join(dateList, ",");
        for (LocalDate date : dateList) {
            LocalDateTime maxTime = LocalDateTime.of(date, LocalTime.MAX);
            LocalDateTime minTime = LocalDateTime.of(date, LocalTime.MIN);
            Map map = new HashMap();
            map.put("end",maxTime);
            Integer totalUser = userMapper.countByMap(map);
            totalUser = totalUser == null ? 0 : totalUser;
            totalUserList.add(totalUser);
            map.put("begin",minTime);
            Integer newUser = userMapper.countByMap(map);
            newUser = newUser == null ? 0 : newUser;
            newUserList.add(newUser);
        }
        String totalUserListString =StringUtils.join(totalUserList, ",");
        String newUserListString =StringUtils.join(newUserList, ",");
        return new UserReportVO(dateListString,totalUserListString,newUserListString);
    }
    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        List<LocalDate> dateList = dateList(begin, end);
        String dateListString = StringUtils.join(dateList, ",");
        for (LocalDate date : dateList) {
            LocalDateTime maxTime = LocalDateTime.of(date, LocalTime.MAX);
            LocalDateTime minTime = LocalDateTime.of(date, LocalTime.MIN);
            Map map = new HashMap();
            map.put("begin",minTime);
            map.put("end",maxTime);
            Integer orderCount = orderMapper.countByMap(map);
            orderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(orderCount);
            map.put("status",5);
            Integer validOrderCount = orderMapper.countByMap(map);
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;
            validOrderCountList.add(validOrderCount);
        }
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        String orderCountListString =StringUtils.join(orderCountList, ",");
        String validOrderCountListString =StringUtils.join(validOrderCountList, ",");
        return new OrderReportVO(dateListString,orderCountListString,validOrderCountListString,totalOrderCount,validOrderCount,orderCompletionRate);
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<Map> mapList = orderMapper.getSalesTop10(begin, end);
        List<String> nameList = new ArrayList<>();
        for (Map map : mapList) {
            nameList.add((String) map.get("name"));
        }
        List<Integer> numberList = new ArrayList<>();
        for (Map map : mapList) {
            numberList.add((Integer) map.get("number"));
        }
        String nameListString = StringUtils.join(nameList, ",");
        String numberListString = StringUtils.join(numberList, ",");
        return new SalesTop10ReportVO(nameListString,numberListString);
    }

    public static  List<LocalDate> dateList (LocalDate begin, LocalDate end){
        List<LocalDate> dateList =new ArrayList<>();
        dateList.add(begin);
        while (begin.isBefore(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}

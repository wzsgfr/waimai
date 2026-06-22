package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    @Options(useGeneratedKeys = true, keyProperty  = "id")
    void insert(Orders orders);

    void insertAll(List<OrderDetail> orderDetails);
    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
    @Select("select * from orders where id = #{id}")
    Orders getByOrderIda(Long id);
    @Update("update orders set status = 6 where id = #{id}")
    void cancel(Long id);

    Page<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();
    @Update("update orders set status = 3 where id = #{id}")
    void confirm(Integer id);
    @Update("update orders set status = 6  , rejection_reason=#{rejectionReason} ,cancel_time=#{cancelTime} where id = #{id}")
    void rejection(Orders orders);
    @Update("update orders set status = 6  ,cancel_reason =#{cancelReason} ,cancel_time=#{cancelTime} where id = #{id}")
    void amindCancel(Orders orders);
    @Update("update orders set status = 4 where id = #{id}")
    void delivery(Long id);
    @Update("update orders set status = 5 ,delivery_time=#{deliveryTime} where id = #{id}")
    void complete(Orders orders);
    @Select("select * from orders where status=1")
    List<Orders> getPaymentOrders();
    @Select("select *from orders where status=4")
    List<Orders> getCancelOrders();
    @Update("update orders set status=5 ,delivery_time=#{deliveryTime} where id=#{id}")
    void estimatedDelivery(Orders orders);

    Double sumByMap(Map map);

    Integer countByMap(Map map);

    List<Map> getSalesTop10(LocalDate begin, LocalDate end);
    @Select("SELECT count(id) from orders where status=#{i}")
    Integer countByStatus(int i);
    @Select("SELECT count(id) from orders  ")
    Integer count();
}

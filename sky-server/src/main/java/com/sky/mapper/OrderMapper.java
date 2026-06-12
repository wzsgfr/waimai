package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

}

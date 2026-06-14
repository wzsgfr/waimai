package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        Long addressBookId=ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook= addressBookMapper.getById(addressBookId);
        if (addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId= BaseContext.getCurrentId();
        List<ShoppingCart>list =shoppingCartMapper.selectAll(userId);
        if (list==null||list.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        User user = userMapper.getById(userId);
        Orders orders=new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());
        orders.setUserName(user.getName());
        orders.setPhone(addressBook.getPhone());
        orderMapper.insert(orders);
        List<OrderDetail>orderDetails=new ArrayList<>();
        for (ShoppingCart cart : list){
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setName(cart.getName());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetail.setImage(cart.getImage());
            orderDetails.add(orderDetail);
        }
        orderMapper.insertAll(orderDetails);
        OrderSubmitVO ordersSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        shoppingCartMapper.delete(userId);
        return ordersSubmitVO;

    }
    /**

     * 订单支付
     *

     * @param ordersPaymentDTO

     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
        JSONObject jsonObject=new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**

     * 支付成功，修改订单状态
     *

     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public PageResult history(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        List<OrderVO> list = new ArrayList<>();
        Page<Orders> page=orderMapper.pageQuery(ordersPageQueryDTO);

        if (page!=null&&page.size()>0){
            for (Orders orders : page) {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                Long orderId = orders.getId();
                List<OrderDetail> orderDetailList = orderMapper.getByOrderId(orderId);
                orderVO.setOrderDetailList(orderDetailList);
                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    @Override
    public OrderVO show(Long id) {
        OrderVO orderVO = new OrderVO();
        Orders orders= orderMapper.getByOrderIda(id);
        BeanUtils.copyProperties(orders,orderVO);
        List<OrderDetail> orderDetailList = orderMapper.getByOrderId(id);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    public void cancel(Long id) {
        orderMapper.cancel(id);
    }

    @Override
    public void repetition(Long id) {
        List<OrderDetail> orderDetailList = orderMapper.getByOrderId(id);
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCartMapper.addAll(shoppingCart);
        }
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.conditionSearch(ordersPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderStatisticsVO statistics() {
       return  orderMapper.statistics();
    }

    @Override
    public void confirm(Integer id) {
        orderMapper.confirm(id);
    }

    @Override
    public void rejection(OrdersRejectionDTO  ordersRejectionDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersRejectionDTO, orders);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.rejection(orders);
    }

    @Override
    public void amindCancel(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersCancelDTO, orders);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.amindCancel(orders);
    }
}

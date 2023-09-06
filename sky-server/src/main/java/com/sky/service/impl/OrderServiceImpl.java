package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderSubmitVO
     * @author paxi
     * @data 2023/9/6
     **/
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 处理各种异常请求，包括但不限于：用户未设置地址，购物车为空等异常请求信息...
        // 判断用户是否添加了收货地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();
        // 判断用户购物车是否有商品
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 向订单表新增一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        // 设置下单用户
        orders.setUserId(userId);
        // 设置下单时间
        orders.setOrderTime(LocalDateTime.now());
        // 设置订单状态，此时为未支付订单
        orders.setStatus(Orders.PENDING_PAYMENT);
        // 设置支付状态
        orders.setPayStatus(Orders.UN_PAID);
        // 生成并设置订单号，按时间戳
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        // 设置收货人
        orders.setConsignee(addressBook.getConsignee());
        ordersMapper.save(orders);

        // 向订单明细表(存储订单中的商品详细信息)插入多条数据
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        // 遍历购物车取到具体数据
        shoppingCarts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.saveList(orderDetailList);

        // 封装前台展示信息(VO)
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        // 清空用户购物车
        shoppingCartMapper.deleteAllByUserId(userId);

        // 返回数据
        return orderSubmitVO;
    }
}

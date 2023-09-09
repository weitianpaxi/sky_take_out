package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderSubmitVO
     * @author paxi
     * @data 2023/9/6
     **/
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 用户订单支付
     * @param ordersPaymentDTO
     * @return com.sky.vo.OrderPaymentVO
     * @author paxi
     * @data 2023/9/7
     **/
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 用户支付成功，修改订单状态
     * @param outTradeNo
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    void paySuccess(String outTradeNo);

    /**
     * 用户查询订单详情
     * @param id
     * @return com.sky.vo.OrderVO
     * @author paxi
     * @data 2023/9/7
     **/
    OrderVO getDetailById(Long id);

    /**
     * 查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/9/7
     **/
    PageResult historyOrders(Integer page, Integer pageSize, Integer status);

    /**
     * 再来一单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    void repetition(Long orderId);

    /**
     * 用户取消订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    void userCancelByOrderId(Long orderId);

    /**
     * 商家接单
     * @param ordersConfirmDTO
     * @return void
     * @author paxi
     * @data 2023/9/8
     **/
    void confirmOrders(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     * @return void
     * @author paxi
     * @data 2023/9/8
     **/
    void rejectOrders(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 商家派送订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    void deliveryOrders(Integer orderId);

    /**
     * 商家完成订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    void completeOrders(Long orderId);

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    void cancelOrders(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单状态查询与统计
     * @return com.sky.vo.OrderStatisticsVO
     * @author paxi
     * @data 2023/9/9
     **/
    OrderStatisticsVO getOrderStatus();

    /**
     * 订单多条件分页查询
     * @param ordersPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/9/9
     **/
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}

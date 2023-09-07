package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
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
}

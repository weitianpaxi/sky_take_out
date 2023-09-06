package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderSubmitVO
     * @author paxi
     * @data 2023/9/6
     **/
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}

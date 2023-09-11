package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时处理类，该类也需要示例化，并交由spring容器管理
 * @author paxi
 * @data 2023/9/11
 **/
@Component
@Slf4j
public class OrdersTask {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 订单支付超时处理类
     * 每分钟触发执行一次
     * @return void
     * @author paxi
     * @data 2023/9/11
     **/
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrders() {
        log.info("处理支付超时的订单：{}", LocalDateTime.now());

        // 处理当前时间十五分钟前的订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);

        if (ordersList != null && !ordersList.isEmpty()) {
            // 遍历超时订单列表，将订单状态改为已取消
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason(MessageConstant.ORDER_TIMEOUT);
                ordersMapper.update(orders);
            });
        }
    }

    /**
     * 订单配送中超时处理
     * 每天的凌晨一点执行一次
     * @return void
     * @author paxi
     * @data 2023/9/11
     **/
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrders() {
        log.info("处理派送中未手动完成的订单：{}", LocalDateTime.now());

        // 处理前一天的所有订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);

        if (ordersList != null && !ordersList.isEmpty()) {
            // 遍历订单列表，将订单状态改为已完成
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                ordersMapper.update(orders);
            });
        }
    }

}

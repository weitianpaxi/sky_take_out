package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "小程序端订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     * @author paxi
     * @data 2023/9/6
     **/
    @PostMapping("/submit")
    @ApiOperation(value = "用户下单")
    public Result<OrderSubmitVO> placeAnOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单信息：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户订单微信支付
     * @param ordersPaymentDTO
     * @return com.sky.result.Result<com.sky.vo.OrderPaymentVO>
     * @author paxi
     * @data 2023/9/7
     **/
    @PutMapping("/payment")
    @ApiOperation(value = "订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 用户查询订单详情
     * @param id
     * @return com.sky.vo.OrderVO
     * @author paxi
     * @data 2023/9/7
     **/
    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "查询订单详情")
    public Result<OrderVO> showOrderDetail(@PathVariable("id") Long id) {
        log.info("查询订单详情编号为：{}",id);
        OrderVO orderVO = orderService.getDetailById(id);
        return Result.success(orderVO);
    }

    /**
     * 小程序端查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/9/7
     **/
    @GetMapping("/historyOrders")
    @ApiOperation(value = "查询历史订单")
    public Result<PageResult> getHistoryOrders(Integer page,Integer pageSize, Integer status) {
        log.info("小程序历史订单查询：{},{},{}",page,pageSize,status);
        PageResult pageResult = orderService.historyOrders(page,pageSize,status);
        return Result.success(pageResult);
    }

    /**
     * 用户再来一单
     * @param orderId
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/7
     **/
    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "再来一单")
    public Result<String> oneMoreOrder(@PathVariable("id") Long orderId) {
        log.info("再来一单的订单ID：{}",orderId);
        orderService.repetition(orderId);
        return Result.success();
    }

    /**
     * 用户取消订单
     * @param orderId
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/7
     **/
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result<String> cancel(@PathVariable("id") Long orderId) {
        log.info("用户取消的订单号：{}",orderId);
        orderService.userCancelByOrderId(orderId);
        return Result.success();
    }

    /**
     * 用户催单
     * @param orderId
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/11
     **/
    @GetMapping("/reminder/{id}")
    @ApiOperation(value = "用户催单")
    public Result<String> reminder(@PathVariable("id") Long orderId) {
        log.info("用户催促订单号：{}",orderId);
        orderService.reminderOrders(orderId);
        return Result.success();
    }
}

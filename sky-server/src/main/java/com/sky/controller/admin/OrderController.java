package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 商家接单
     * @param ordersConfirmDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/8
     **/
    @PutMapping("/confirm")
    @ApiOperation(value = "接单")
    public Result<String> acceptOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("商家接单订单信息：{}",ordersConfirmDTO);
        orderService.confirmOrders(ordersConfirmDTO);
        return  Result.success();
    }

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/8
     **/
    @PutMapping("/rejection")
    @ApiOperation(value = "拒单")
    public Result<String> rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("商家拒绝订单信息：{}",ordersRejectionDTO);
        orderService.rejectOrders(ordersRejectionDTO);
        return  Result.success();
    }

    /**
     * 商家派送订单
     * @param orderId
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/9
     **/
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "派送订单")
    public Result<String> deliveryOrder(@PathVariable("id") Integer orderId) {
        log.info("派送订单为：{}",orderId);
        orderService.deliveryOrders(orderId);
        return Result.success();
    }

    /**
     * 商家完成订单
     * @param orderId
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/9
     **/
    @PutMapping("complete/{id}")
    @ApiOperation(value = "完成订单")
    public Result<String> completeOrder(@PathVariable("id") Long orderId) {
        log.info("完成订单为：{}",orderId);
        orderService.completeOrders(orderId);
        return Result.success();
    }

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/9
     **/
    @PutMapping("/cancel")
    @ApiOperation(value = "取消订单")
    public Result<String> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("商家取消订单：{}",ordersCancelDTO);
        orderService.cancelOrders(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 商家查看订单详情
     * @param orderId
     * @return com.sky.result.Result<com.sky.vo.OrderVO>
     * @author paxi
     * @data 2023/9/9
     **/
    @GetMapping("/details/{id}")
    @ApiOperation(value = "查看订单详情")
    public Result<OrderVO> showOrderDetails(@PathVariable("id") Long orderId) {
        log.info("商家管理端查看订单详情ID：{}",orderId);
        OrderVO orderVO = orderService.getDetailById(orderId);
        return  Result.success(orderVO);
    }

    /**
     * 订单状态查询统计
     * @return com.sky.result.Result<com.sky.vo.OrderStatisticsVO>
     * @author paxi
     * @data 2023/9/9
     **/
    @GetMapping("/statistics")
    @ApiOperation(value = "订单状态查询与统计")
    public Result<OrderStatisticsVO> getOrderStatus() {
        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatus();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 商家订单多条件分页查询
     * @param ordersPageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/9/9
     **/
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "订单多条件分页查询")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单管理接受参数：{}",ordersPageQueryDTO);
        PageResult pageResult =  orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
}

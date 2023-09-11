package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;

    // 查询参数封装用map集合
    private final Map<String,Integer> paramMap = new HashMap<>();

    /**
     * 获得套餐总览
     * @return com.sky.vo.SetmealOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    @Override
    public SetmealOverViewVO getSetmealsOverview() {
        paramMap.put("status", StatusConstant.ENABLE);
        Integer sold =  setmealMapper.getCount(paramMap);
        paramMap.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.getCount(paramMap);
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获得菜品总览
     * @return com.sky.vo.DishOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    @Override
    public DishOverViewVO getDishesOverview() {
        paramMap.put("status", StatusConstant.ENABLE);
        Integer sold =  dishMapper.getCount(paramMap);
        paramMap.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.getCount(paramMap);
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获得当日订单总览
     * @return com.sky.vo.OrderOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    @Override
    public OrderOverViewVO getOrderOverview() {
        Map<Object,Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        //待接单
        Integer waitingOrders = ordersMapper.countByMap(map);

        //待派送
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = ordersMapper.countByMap(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = ordersMapper.countByMap(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = ordersMapper.countByMap(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = ordersMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 获得当日运营数据
     * @param begin
     * @param end
     * @return com.sky.vo.BusinessDataVO
     * @author paxi
     * @data 2023/9/10
     **/
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map<Object,Object> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);

        // 查询总订单数
        Integer totalOrderCount = ordersMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        // 营业额
        Double turnover = ordersMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        // 有效订单数
        Integer validOrderCount = ordersMapper.countByMap(map);

        double unitPrice = 0.0;

        double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            // 订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            // 平均客单价
            unitPrice = turnover / validOrderCount;
        }

        // 新增用户数
        Integer newUsers = userMapper.countByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }
}

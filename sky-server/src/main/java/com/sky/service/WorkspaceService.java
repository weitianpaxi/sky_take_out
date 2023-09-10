package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 获得套餐总览
     * @return com.sky.vo.SetmealOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    SetmealOverViewVO getSetmealsOverview();

    /**
     * 获得菜品总览
     * @return com.sky.vo.DishOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    DishOverViewVO getDishesOverview();

    /**
     * 获得当日订单总览
     * @return com.sky.vo.OrderOverViewVO
     * @author paxi
     * @data 2023/9/10
     **/
    OrderOverViewVO getOrderOverview();

    /**
     * 获得当日运营数据
     * @param begin
     * @param end
     * @return com.sky.vo.BusinessDataVO
     * @author paxi
     * @data 2023/9/10
     **/
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);
}

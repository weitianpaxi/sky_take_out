package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 查询套餐总览
     * @return com.sky.result.Result<com.sky.vo.SetmealOverViewVO>
     * @author paxi
     * @data 2023/9/10
     **/
    @GetMapping("/overviewSetmeals")
    @ApiOperation(value = "查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals() {
        SetmealOverViewVO setmealOverViewVO = workspaceService.getSetmealsOverview();
        return Result.success(setmealOverViewVO);
    }

    /**
     * 查询菜品总览
     * @return com.sky.result.Result<com.sky.vo.DishOverViewVO>
     * @author paxi
     * @data 2023/9/10
     **/
    @GetMapping("/overviewDishes")
    @ApiOperation(value = "查询菜品总览")
    public Result<DishOverViewVO> overviewDishes() {
        DishOverViewVO dishOverViewVO = workspaceService.getDishesOverview();
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询订单管理数据
     * @return com.sky.result.Result<com.sky.vo.OrderOverViewVO>
     * @author paxi
     * @data 2023/9/10
     **/
    @GetMapping("/overviewOrders")
    @ApiOperation(value = "查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders() {
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverview();
        return Result.success(orderOverViewVO);
    }

    /**
     * 查询今日运营数据
     * @return com.sky.result.Result<com.sky.vo.BusinessDataVO>
     * @author paxi
     * @data 2023/9/10
     **/
    @GetMapping("/businessData")
    @ApiOperation(value = "查询今日运营数据")
    public Result<BusinessDataVO> businessData() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }
}

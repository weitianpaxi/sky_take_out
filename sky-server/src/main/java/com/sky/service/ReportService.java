package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 获取指定时间段内的营业额数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return com.sky.vo.TurnoverReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    TurnoverReportVO getTurnoverData(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内的用户数数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return com.sky.vo.UserReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    UserReportVO getUserData(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内的订单详细数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return com.sky.vo.OrderReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    OrderReportVO getOrdersData(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内商品的销量数前十的数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return com.sky.vo.SalesTop10ReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    SalesTop10ReportVO getTopTenData(LocalDate begin, LocalDate end);
}

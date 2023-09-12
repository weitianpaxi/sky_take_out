package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param begin 开始日期
     * @param end 结束日期
     * @return com.sky.result.Result<com.sky.vo.TurnoverReportVO>
     * @author paxi
     * @data 2023/9/11
     **/
    @GetMapping("/turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询营业额数据开始日期：{}，结束日期：{}",begin,end);
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverData(begin,end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 用户数据统计
     * @param begin
     * @param end
     * @return com.sky.result.Result<com.sky.vo.UserReportVO>
     * @author paxi
     * @data 2023/9/11
     **/
    @GetMapping("/userStatistics")
    @ApiOperation(value = "用户数据统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询用户数据开始日期：{}，结束日期：{}",begin,end);
        return Result.success(reportService.getUserData(begin,end));
    }

    /**
     * 订单数据统计
     * @param begin
     * @param end
     * @return com.sky.result.Result<com.sky.vo.OrderReportVO>
     * @author paxi
     * @data 2023/9/11
     **/
    @GetMapping("/ordersStatistics")
    @ApiOperation(value = "订单数据统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询订单数据开始日期：{}，结束日期：{}",begin,end);
        return Result.success(reportService.getOrdersData(begin,end));
    }

    /**
     * 查询销量前十数据统计
     * @param begin
     * @param end
     * @return com.sky.result.Result<com.sky.vo.SalesTop10ReportVO>
     * @author paxi
     * @data 2023/9/11
     **/
    @GetMapping("/top10")
    @ApiOperation(value = "查询销量前十")
    public Result<SalesTop10ReportVO> salesTop10Statistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("查询销量前十数据开始日期：{}，结束日期：{}",begin,end);
        return Result.success(reportService.getTopTenData(begin,end));
    }

    /**
     * 导出Excel报表
     * @param response 输出流
     * @return void
     * @author paxi
     * @data 2023/9/12
     **/
    @GetMapping("/export")
    @ApiOperation(value = "导出Excel报表")
    public void export(HttpServletResponse response) {
        reportService.exportBusinessData(response);
    }
}

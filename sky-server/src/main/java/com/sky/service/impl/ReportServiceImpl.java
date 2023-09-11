package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取指定时间段内的营业额数据
     * @param begin 开始日期
     * @param end   结束日期
     * @return com.sky.vo.TurnoverReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    @Override
    public TurnoverReportVO getTurnoverData(LocalDate begin, LocalDate end) {
        // 1. 首先获得从开始begin到结束end的dateList
        List<LocalDate> dateList = getDateList(begin, end);

        // 2. 再查询datalist中每一天的营业额，生成turnoverList
        List<Double> turnoverList = new ArrayList<>();
        dateList.forEach(date ->{
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = ordersMapper.sumByMap(map);
            // 若当天没有订单，则此查询返回值为空，为了数据展示，则显示为0
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        });

        // 3. 封装返回数据对象
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 获取指定时间段内的用户数数据
     * @param begin 开始日期
     * @param end   结束日期
     * @return com.sky.vo.UserReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    @Override
    public UserReportVO getUserData(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        // 每天总的用户数
        List<Integer> allUserList = new ArrayList<>();
        // 每天新注册的用户数
        List<Integer> newUserList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("end",endTime);
            // 总用户数量 select count(id) from user where creat_time < 某天的最后一刻
            Integer allUserNumber = userMapper.countByMap(map);
            allUserList.add(allUserNumber);
            map.put("begin",beginTime);
            // 某日新增用户数量 select count(id) from user where creat_time < 某天的最后一刻 and creat_time > 某天开始时刻
            Integer newUserNumber = userMapper.countByMap(map);
            newUserList.add(newUserNumber);
        });

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(allUserList,","))
                .build();
    }

    /**
     * 获取指定时间段内的订单详细数据
     * @param begin 开始日期
     * @param end   结束日期
     * @return com.sky.vo.OrderReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    @Override
    public OrderReportVO getOrdersData(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        // 每天总订单数
        List<Integer> allOrderList = new ArrayList<>();
        // 每天有效订单数
        List<Integer> validOrderList = new ArrayList<>();

        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 查询每天总订单数
            Integer allOrderCount = getOrdersCount(beginTime,endTime,null);
            allOrderList.add(allOrderCount);
            // 查询每天有效订单数
            Integer validOrderCount = getOrdersCount(beginTime,endTime,Orders.COMPLETED);
            validOrderList.add(validOrderCount);
        });

        // 利用流对象遍历集合获得每个元素相加后的值
        // 计算时间段内总订单数
        Integer allOrdersCount = allOrderList.stream().reduce(Integer::sum).get();
        // 计算时间段内有效订单数
        Integer validOrdersCount = validOrderList.stream().reduce(Integer::sum).get();
        // 计算订单完成率
        Double orderCompletionRate = 0.0;
        if (allOrdersCount != 0) {
            orderCompletionRate = validOrdersCount.doubleValue() / allOrdersCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(allOrderList,","))
                .validOrderCountList(StringUtils.join(validOrderList,","))
                .totalOrderCount(allOrdersCount)
                .validOrderCount(validOrdersCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 获取指定时间段内商品的销量数前十的数据
     * @param begin 开始日期
     * @param end   结束日期
     * @return com.sky.vo.SalesTop10ReportVO
     * @author paxi
     * @data 2023/9/11
     **/
    @Override
    public SalesTop10ReportVO getTopTenData(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSealTopTenList = ordersMapper.getGoodsSealTopTen(beginTime, endTime);

        List<String> nameList = goodsSealTopTenList.stream()
                                                   .map(GoodsSalesDTO::getName)
                                                   .collect(Collectors.toList());
        List<Integer> numberList = goodsSealTopTenList.stream()
                                                      .map(GoodsSalesDTO::getNumber)
                                                      .collect(Collectors
                                                      .toList());

        return SalesTop10ReportVO.builder()
                                 .nameList(StringUtils.join(nameList,","))
                                 .numberList(StringUtils.join(numberList,","))
                                 .build();
    }

    /**
     * 获取指定时间段的日期列表
     * @param begin
     * @param end
     * @return java.util.List<java.time.LocalDate>
     * @author paxi
     * @data 2023/9/11
     **/
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }

    /**
     * 获得某段时间内指定状态的订单数量
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/11
     **/
    private Integer getOrdersCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map<Object,Object> map = new HashMap<>();
        map.put("begin",beginTime);
        map.put("end",endTime);
        map.put("status", status);
        return ordersMapper.countByMap(map);
    }
}

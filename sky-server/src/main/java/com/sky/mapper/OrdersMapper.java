package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    /**
     * 新增订单数据
     * @param orders
     * @return void
     * @author paxi
     * @data 2023/9/6
     **/
    void save(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return com.sky.entity.Orders
     * @author paxi
     * @data 2023/9/7
     **/
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 更新修改订单数据
     * @param orders
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    void update(Orders orders);

    /**
     * 根据ID查询订单信息
     * @param id
     * @return com.sky.entity.Orders
     * @author paxi
     * @data 2023/9/7
     **/
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return com.github.pagehelper.Page<com.sky.entity.Orders>
     * @author paxi
     * @data 2023/9/7
     **/
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计不同状态的订单数
     * @param status
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/9
     **/
    @Select("select count(id) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 按参数统计订单
     * @param map
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/10
     **/
    Integer countByMap(Map<Object, Object> map);

    /**
     * 统计某时间段内营业额数据
     * @param map
     * @return java.lang.Double
     * @author paxi
     * @data 2023/9/10
     **/
    Double sumByMap(Map<Object, Object> map);

    /**
     * 根据状态和时间查询订单
     * @param status
     * @param time
     * @return java.util.List<com.sky.entity.Orders>
     * @author paxi
     * @data 2023/9/11
     **/
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    /**
     * 统计某段时间内商品销量前十的商品名称和数量
     * @param beginTime
     * @param endTime
     * @return java.util.List<com.sky.dto.GoodsSalesDTO>
     * @author paxi
     * @data 2023/9/11
     **/
    List<GoodsSalesDTO> getGoodsSealTopTen(LocalDateTime beginTime, LocalDateTime endTime);
}

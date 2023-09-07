package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}

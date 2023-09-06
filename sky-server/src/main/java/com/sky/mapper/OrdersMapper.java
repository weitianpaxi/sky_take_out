package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}

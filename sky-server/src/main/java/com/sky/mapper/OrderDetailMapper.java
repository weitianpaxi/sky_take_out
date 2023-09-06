package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详细数据
     * @param orderDetailList
     * @return void
     * @author paxi
     * @data 2023/9/6
     **/
    void saveList(ArrayList<OrderDetail> orderDetailList);
}

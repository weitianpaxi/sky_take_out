package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据订单号查询订单详情
     * @param id
     * @return java.util.List<com.sky.entity.OrderDetail>
     * @author paxi
     * @data 2023/9/7
     **/
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);
}

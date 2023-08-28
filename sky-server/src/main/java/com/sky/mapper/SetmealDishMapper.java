package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID查询套餐ID
     * @param dishIds
     * @return java.util.List<java.lang.Long>
     * @author paxi
     * @data 2023/8/28
     **/
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}

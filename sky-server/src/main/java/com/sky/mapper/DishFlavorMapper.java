package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 向菜品表批量插入数据
     * @param dishFlavorList
     * @return void
     * @author paxi
     * @data 2023/8/27
     **/
    void insertBatch(List<DishFlavor> dishFlavorList);

    /**
     * 根据菜品ID删除对应口味数据
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/28
     **/
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
}

package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
     * @param dishId
     * @return void
     * @author paxi
     * @data 2023/8/28
     **/
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品ID查询对应口味信息
     * @param dishId
     * @return java.util.List<com.sky.entity.DishFlavor>
     * @author paxi
     * @data 2023/8/29
     **/
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}

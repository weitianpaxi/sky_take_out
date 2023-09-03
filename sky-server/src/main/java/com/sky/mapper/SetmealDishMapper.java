package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 新增套餐与菜品对应关系
     * @param setmealDishes
     * @return void
     * @author paxi
     * @data 2023/8/31
     **/
    void saveWithDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID查询其对应菜品列表
     * @param setmealId
     * @return java.util.List<com.sky.entity.SetmealDish>
     * @author paxi
     * @data 2023/9/2
     **/
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐ID删除对应的菜品信息
     * @param setmealId
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);
}

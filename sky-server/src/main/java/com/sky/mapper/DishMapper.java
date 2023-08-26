package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 菜品管理相关数据库操作
 * @author paxi
 * @data 2023/8/26
 **/
@Mapper
public interface DishMapper {

    /**
     * 根据分类ID查询菜品数量
     * @param categoryId
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/8/26
     **/
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}

package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐管理相关数据库操作
 * @author paxi
 * @data 2023/8/26
 **/
@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID查询套餐的数量
     * @param id
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/8/26
     **/
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
}

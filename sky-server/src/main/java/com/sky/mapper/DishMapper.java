package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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

    /**
     * 插入菜品数据
     * @param dish
     * @return void
     * @author paxi
     * @data 2023/8/27
     **/
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return com.github.pagehelper.Page<com.sky.vo.DishVO>
     * @author paxi
     * @data 2023/8/28
     **/
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键查询菜品
     * @param id
     * @return com.sky.entity.Dish
     * @author paxi
     * @data 2023/8/28
     **/
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据主键删除菜品
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/28
     **/
    @Delete("delete dish from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新菜品数据
     * @param dish
     * @return void
     * @author paxi
     * @data 2023/8/29
     **/
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return java.util.List<com.sky.entity.Dish>
     * @author paxi
     * @data 2023/8/29
     **/
    List<Dish> getByCategoryId(Integer categoryId);

    /**
     * 根据套餐ID查询菜品信息
     * @param setmealId
     * @return java.util.List<com.sky.entity.Dish>
     * @author paxi
     * @data 2023/9/3
     **/
    @Select("select d.* from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据不同条件查询菜品，用于小程序端
     * @param dish
     * @return java.util.List<com.sky.entity.Dish>
     * @author paxi
     * @data 2023/9/4
     **/
    List<Dish> list(Dish dish);

    /**
     * 根据状态统计数量
     * @param paramMap
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/10
     **/
    Integer getCount(Map<String, Integer> paramMap);
}

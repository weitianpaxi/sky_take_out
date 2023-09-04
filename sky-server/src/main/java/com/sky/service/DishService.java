package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService {

   /**
    * 新增菜品和对应的口味数据
    * @param dishDTO
    * @return void
    * @author paxi
    * @data 2023/8/27
    **/
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/28
     **/
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID批量删除ID
     * @param ids
     * @return void
     * @author paxi
     * @data 2023/8/28
     **/
    void deleteByIds(List<Long> ids);
    /**
     * 根据ID查询菜品和对应的口味信息
     * @param id
     * @return com.sky.vo.DishVO
     * @author paxi
     * @data 2023/8/29
     **/
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品基本信息以及相对应的口味信息
     * @param dishDTO
     * @return void
     * @author paxi
     * @data 2023/8/29
     **/
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启售，停售菜品
     * @param id
     * @param status
     * @return void
     * @author paxi
     * @data 2023/8/29
     **/
    void startAndStop(Long id, Integer status);
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return java.util.List<com.sky.entity.Dish>
     * @author paxi
     * @data 2023/8/29
     **/
    List<Dish> getByCategoryId(Integer categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return java.util.List<com.sky.vo.DishVO>
     * @author paxi
     * @data 2023/9/4
     **/
    List<DishVO> listWithFlavor(Dish dish);
}

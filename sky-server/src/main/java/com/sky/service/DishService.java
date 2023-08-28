package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
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
}

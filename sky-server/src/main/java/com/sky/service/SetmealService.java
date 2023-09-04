package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO
     * @return void
     * @author paxi
     * @data 2023/8/31
     **/
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/31
     **/
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ID查询套餐
     * @param id
     * @return com.sky.vo.SetmealVO
     * @author paxi
     * @data 2023/9/2
     **/
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    void update(SetmealDTO setmealDTO);

    /**
     * 起售和禁售套餐
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    void startAndStop(Integer status, Long id);

    /**
     * 根据ID批量删除套餐
     * @param ids
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    void deleteByIds(List<Long> ids);

    /**
     * 小程序端条件查询套餐
     * @param setmeal
     * @return java.util.List<com.sky.entity.Setmeal>
     * @author paxi
     * @data 2023/9/4
     **/
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 小程序端根据分类id查询套餐
     * @param id
     * @return java.util.List<com.sky.vo.DishItemVO>
     * @author paxi
     * @data 2023/9/4
     **/
    List<DishItemVO> getDishItemById(Long id);
}

package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


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

    /**
     * 新增套餐
     * @param setmeal
     * @return void
     * @author paxi
     * @data 2023/8/31
     **/
    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return com.github.pagehelper.Page<com.sky.vo.SetmealVO>
     * @author paxi
     * @data 2023/8/31
     **/
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ID查询套餐
     * @param id
     * @return com.sky.vo.SetmealVO
     * @author paxi
     * @data 2023/9/2
     **/
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 更新套餐信息
     * @param setmeal
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据ID删除套餐信息
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    /**
     * 小程序端题条件查询套餐
     * @param setmeal
     * @return java.util.List<com.sky.entity.Setmeal>
     * @author paxi
     * @data 2023/9/4
     **/
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 小程序端根据套餐id查询菜品选项
     * @param setmealId
     * @return java.util.List<com.sky.vo.DishItemVO>
     * @author paxi
     * @data 2023/9/4
     **/
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据状态统计数量
     * @param paramMap
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/10
     **/
    Integer getCount(Map<String, Integer> paramMap);
}

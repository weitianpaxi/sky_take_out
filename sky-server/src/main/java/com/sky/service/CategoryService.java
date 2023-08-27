package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     * @param categoryDTO
     * @return java.lang.Boolean
     * @author paxi
     * @data 2023/8/23
     **/
    Boolean save(CategoryDTO categoryDTO);

    /**
     * 启用，禁用分类
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/23
     **/
    Boolean startAndStop(Integer status, long id);

    /**
     * 修改分类信息
     * @param categoryDTO
     * @return java.lang.Boolean
     * @author paxi
     * @data 2023/8/23
     **/
    Boolean update(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/23
     **/
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据分类删除ID
     * @param id
     * @return boolean
     * @author paxi
     * @data 2023/8/26
     **/
    boolean deleteById(long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return java.util.List<com.sky.entity.Category>
     * @author paxi
     * @data 2023/8/27
     **/
    List<Category> getByType(Integer type);
}

package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public Boolean save(CategoryDTO categoryDTO) {
        Category category = new Category();
        // 属性拷贝
        BeanUtils.copyProperties(categoryDTO,category);

        // 分类默认是禁用状态
        category.setStatus(StatusConstant.DISABLE);

        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        return categoryMapper.save(category) > 0;
    }

    /**
     * 启用和禁用分类
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/23
     **/
    @Override
    public Boolean startAndStop(Integer status, long id) {
        Category category =  Category.builder()
                .id(id)
                .status(status)
                .build();
        return categoryMapper.update(category) > 0;
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return java.lang.Boolean
     * @author paxi
     * @data 2023/8/23
     **/
    @Override
    public Boolean update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        return categoryMapper.update(category) > 0;
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/23
     **/
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page =  categoryMapper.pageQuery(categoryPageQueryDTO);

        long total = page.getTotal();
        List<Category> categoryList = page.getResult();
        return new PageResult(total,categoryList);
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return boolean
     * @author paxi
     * @data 2023/8/26
     **/
    @Override
    public boolean deleteById(long id) {
        // 查询当前分类是否关联了菜品，如果关联则无法删除，抛出异常
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        // 查询当前分类是否关联了套餐，如果关联则无法删除，抛出异常
        count = setmealMapper.countByCategoryId(id);
        if (count > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        return categoryMapper.deleteById(id) > 0;
    }
}

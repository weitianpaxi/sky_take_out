package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品分类管理
 * @author paxi
 * @data 2023/8/22
 **/
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类操作相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /**
     * 新增分类
     * @param categoryDTO
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/8/23
     **/
   @PostMapping
   @ApiOperation(value = "新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
       log.info("新增分类：{}",categoryDTO);
        if (categoryService.save(categoryDTO))
            return Result.success();
        else
            return Result.error(MessageConstant.SAVE_CATEGORY_ERROR);
    }

    /**
     * 启用和禁用分类
     * @param status
     * @param id
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/23
     **/
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用和禁用分类")
    public Result<String> startAndStop(@PathVariable Integer status, long id) {
       log.info("启用，禁用分类：{},{}",status,id);
       if (categoryService.startAndStop(status,id))
           return Result.success();
       else
           return Result.error(MessageConstant.START_AND_STOP_CATEGORY_ERROR);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/23
     **/
    @PutMapping
    @ApiOperation(value = "修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}",categoryDTO);
        if (categoryService.update(categoryDTO))
            return Result.success();
        else
            return Result.error(MessageConstant.UPDATE_CATEGORY_ERROR);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/8/27
     **/
    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/27
     **/
    @DeleteMapping
    @ApiOperation(value = "根据ID删除分类")
    public Result<String> deleteById(long id) {
        log.info("要删除的ID为：{}",id);
        if (categoryService.deleteById(id))
            return Result.success();
        else
            return Result.error(MessageConstant.DELETE_CATEGORY_ERROR);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return com.sky.result.Result<java.util.List<com.sky.entity.Category>>
     * @author paxi
     * @data 2023/8/27
     **/
    @GetMapping("/list")
    @ApiOperation(value = "根据类型查询分类")
    public Result<List<Category>> getByType(Integer type) {
        List<Category> categoryList = categoryService.getByType(type);
        return Result.success(categoryList);
    }
}

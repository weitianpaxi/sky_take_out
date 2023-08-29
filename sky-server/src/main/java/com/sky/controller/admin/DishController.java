package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 * @author paxi
 * @data 2023/8/27
 **/
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/27
     **/
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/8/28
     **/
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID批量删除菜品
     * @param ids 添加@RequestParam 注解可以将传入的按都好分割的字符串解析后存入List中
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/8/28
     **/
    @DeleteMapping
    @ApiOperation(value = "根据ID批量删除菜品")
    public Result deleteDishByIds(@RequestParam List<Long> ids) {
        log.info("菜品批量删除的ID：{}",ids);
        dishService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     * @param id
     * @return com.sky.result.Result<com.sky.vo.DishVO>
     * @author paxi
     * @data 2023/8/29
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据ID查询菜品信息：{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/29
     **/
    @PutMapping
    @ApiOperation(value = "修改菜品信息接口")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 启售，停售菜品
     * @param status
     * @param id
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/29
     **/
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启售，停售菜品")
    public Result<String> startAndStop(@PathVariable Integer status, Long id) {
        log.info("启售，停售菜品：{},{}",id,status);
        dishService.startAndStop(id,status);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return com.sky.result.Result<java.util.List<com.sky.entity.Dish>>
     * @author paxi
     * @data 2023/8/29
     **/
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(Integer categoryId) {
        log.info("根据分类id查询菜品：{}",categoryId);
        List<Dish> dishList = dishService.getByCategoryId(categoryId);
        return Result.success(dishList);
    }
}

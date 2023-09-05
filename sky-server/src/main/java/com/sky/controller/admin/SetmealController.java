package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/31
     **/
    @PostMapping
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    @ApiOperation(value = "新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/8/31
     **/
    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询套餐
     * @param id
     * @return com.sky.result.Result<java.util.List<com.sky.vo.SetmealVO>>
     * @author paxi
     * @data 2023/9/2
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据ID查询套餐：{}",id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/3
     **/
    @PutMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @ApiOperation(value = "修改套餐信息")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐：{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 开始售卖套餐和禁售套餐
     * @param status
     * @param id
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/3
     **/
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @ApiOperation(value = "起售和禁售套餐")
    public Result<String> startAndStop(@PathVariable Integer status,Long id) {
        log.info("起售和禁售套餐：{},{}",status,id);
        setmealService.startAndStop(status,id);
        return Result.success();
    }

    /**
     * 根据ID批量删除套餐
     * @param ids @RequestParam 添加此注解可以自动将字符串参数自动分割转换为所需的字符
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/3
     **/
    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @ApiOperation(value = "根据ID批量删除套餐")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐：{}",ids);
        setmealService.deleteByIds(ids);
        return Result.success();
    }
}

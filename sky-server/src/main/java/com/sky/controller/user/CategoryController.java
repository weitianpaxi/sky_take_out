package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "小程序端 分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @param type
     * @return com.sky.result.Result<java.util.List<com.sky.entity.Category>>
     * @author paxi
     * @data 2023/9/4
     **/
    @GetMapping("/list")
    @ApiOperation(value = "查询分类")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }
}

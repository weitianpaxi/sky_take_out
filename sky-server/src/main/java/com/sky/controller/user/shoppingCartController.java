package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "小程序端购物车相关接口")
public class shoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/5
     **/
    @PostMapping("/add")
    @ApiOperation(value = "购物车添加商品")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车添加商品：{}",shoppingCartDTO);
        shoppingCartService.addshoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "展示购物车")
    public Result<List<ShoppingCart>> show() {
        List<ShoppingCart> shoppingCarts = shoppingCartService.showShoppingCart();
        return Result.success(shoppingCarts);
    }

    /**
     * 删除购物车中的单个商品数据
     * @param shoppingCartDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/5
     **/
    @PostMapping("/sub")
    @ApiOperation(value = "删除购物车中的单个商品")
    public Result<String> deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中的单个商品：{}",shoppingCartDTO);
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车信息
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/5
     **/
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车")
    public Result<String> deleteAll() {
        shoppingCartService.deleteShoppingCart();
        return Result.success();
    }
}

package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

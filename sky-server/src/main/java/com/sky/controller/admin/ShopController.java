package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

// 为防止冲突，设置bean的自定义名字，亦作区分
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺操作接口")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String key = "SHOP_STATUS";

    /**
     * 设置店铺营业状态
     * @param status
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/3
     **/
    @PutMapping("/{status}")
    @ApiOperation(value = "设置店铺营业状态")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为：{}",status == 1 ? "营业中":"打烊了");
        redisTemplate.opsForValue().set(key,status);
        return Result.success();
    }

    /**
     * 查询店铺营业状态
     * @return com.sky.result.Result<java.lang.Integer>
     * @author paxi
     * @data 2023/9/3
     **/
    @GetMapping("/status")
    @ApiOperation(value = "查询店铺营业状态")
    public Result<Integer> getStatus() {
        log.info("管理端查询店铺营业状态...");
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(key);
        log.info("查得店铺营业状态为：{}",shopStatus == 1 ? "营业中":"打烊了");
        return Result.success(shopStatus);
    }
}

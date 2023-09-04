package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "小程序端菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return com.sky.result.Result<java.util.List<com.sky.vo.DishVO>>
     * @author paxi
     * @data 2023/9/4
     **/
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 设置redis的key 规则：dish_分类ID
        String key = "dish_" + categoryId;

        // 查询redis中是否存在key所对应的值
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // 如果存在 则直接返回数据
        if (list != null && !list.isEmpty()) {
            return Result.success(list);
        }
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        // 查询起售中的菜品
        dish.setStatus(StatusConstant.ENABLE);
        // 缓存中不存在数据则从数据库中查询
        list = dishService.listWithFlavor(dish);
        // 将查询到的数据存入redis
        redisTemplate.opsForValue().set(key,list);

        return Result.success(list);
    }

}

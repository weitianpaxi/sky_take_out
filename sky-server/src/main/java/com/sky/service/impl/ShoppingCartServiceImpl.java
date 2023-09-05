package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Override
    public void addshoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 判断当前商品是否已经存在于购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getList(shoppingCart);

        // 若商品存在，则只需要将商品数量number加1
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            ShoppingCart newShoppingCart = shoppingCartList.get(0);
            newShoppingCart.setNumber(newShoppingCart.getNumber() + 1);
            shoppingCartMapper.updatenumberById(newShoppingCart);
        }else {
            // 若商品不存在，则向购物车表新增一条数据
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setName(dish.getName());
            } else {
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(shoppingCart);
        }
    }

    /**
     * 展示购物车数据
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @author paxi
     * @data 2023/9/5
     **/
    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 获取当前登录的微信用户的ID
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        return shoppingCartMapper.getList(shoppingCart);
    }

    /**
     * 删除购物车中的单个商品数据
     * @param shoppingCartDTO
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Override
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        // 拿到用户所对应的购物车信息
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        if (shoppingCarts != null && !shoppingCarts.isEmpty()) {
            // 遍历购物车，按接收到的dishID和setmealID将对应数量减一
            shoppingCart = shoppingCarts.get(0);
            // 获得商品数量
            Integer number = shoppingCart.getNumber();
            if (number == 1) {
                // 只有一份商品，删除当前记录
                shoppingCartMapper.deleteOneById(shoppingCart.getId());
            }else {
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updatenumberById(shoppingCart);
            }
        }

    }

    /**
     * 清空购物车信息
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Override
    public void deleteShoppingCart() {
        // 是当前登录的用户删除其所属的购物车信息
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteAllByUserId(userId);
    }
}

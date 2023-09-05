package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询某个用户的购物车数据
     * @param shoppingCart
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @author paxi
     * @data 2023/9/5
     **/
    List<ShoppingCart> getList(ShoppingCart shoppingCart);

    /**
     * 根据ID更新购物车中商品的数量
     * @param newShoppingCart
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updatenumberById(ShoppingCart newShoppingCart);

    /**
     * 向购物车新增购物数据
     * @param shoppingCart
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void save(ShoppingCart shoppingCart);
}

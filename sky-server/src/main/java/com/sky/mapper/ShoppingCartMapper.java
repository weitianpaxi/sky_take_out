package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 根据用户ID删除购物车信息
     * @param userId
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteAllByUserId(Long userId);

    /**
     * 根据ID删除单条数据
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteOneById(Long id);
}

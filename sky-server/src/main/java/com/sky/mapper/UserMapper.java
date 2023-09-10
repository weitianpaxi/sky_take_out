package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openId查询用户
     * @param openId
     * @return com.sky.entity.User
     * @author paxi
     * @data 2023/9/4
     **/
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openId);

    /**
     * 新增用户
     * @param user
     * @return com.sky.entity.User
     * @author paxi
     * @data 2023/9/4
     **/
    User save(User user);

    /**
     * 根据ID查询用户
     * @param userId
     * @return com.sky.entity.User
     * @author paxi
     * @data 2023/9/7
     **/
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/10
     **/
    Integer countByMap(Map<Object, Object> map);
}

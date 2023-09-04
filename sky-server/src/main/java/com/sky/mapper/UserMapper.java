package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}

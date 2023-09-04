package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return com.sky.entity.User
     * @author paxi
     * @data 2023/9/4
     **/
    User weichatlogin(UserLoginDTO userLoginDTO);
}

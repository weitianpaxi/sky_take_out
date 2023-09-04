package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    // 微信登录服务接口地址，请求方式为GET
    public static final String WEICHAT_USER_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";


    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return com.sky.entity.User
     * @author paxi
     * @data 2023/9/4
     **/
    @Override
    public User weichatlogin(UserLoginDTO userLoginDTO) {

        // 调用微信接口，获取当前微信用户的openID
        String openId = getUserOpenId(userLoginDTO.getCode());

        // 判断openID是否为空，若为空，则登录失败，抛出业务异常，用户登录失败
        if (openId == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断是否为新用户
        User user = userMapper.getByOpenId(openId);

        // 是新用户，自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.save(user);
        }

        // 返回用户对象
        return user;
    }

    /**
     * 调用微信登录接口，获取用户openID
     * @param code
     * @return java.lang.String
     * @author paxi
     * @data 2023/9/4
     **/
    private String getUserOpenId(String code) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("appid",weChatProperties.getAppid());
        userMap.put("secret",weChatProperties.getSecret());
        userMap.put("js_code",code);
        userMap.put("grant_type","authorization_code");
        String str_json = HttpClientUtil.doGet(WEICHAT_USER_LOGIN, userMap);
        JSONObject jsonObject = JSON.parseObject(str_json);
        return jsonObject.getString("openid");
    }
}

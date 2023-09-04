package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "小程序端用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;
    // 注入JWT配置类
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return com.sky.result.Result<com.sky.vo.UserLoginVO>
     * @author paxi
     * @data 2023/9/4
     **/
    @PostMapping("/login")
    @ApiOperation(value = "微信用户登录")
    public Result<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录授权码：{}",userLoginDTO);
        // 微信登录
        User user = userService.weichatlogin(userLoginDTO);
        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        // 设置用户唯一标识
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String tocken = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        // 封装VO对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(tocken)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * 微信用户退出登录
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/9/4
     **/
    @PostMapping("/logout")
    @ApiOperation(value = "微信用户退出登录")
    public Result<String> logout() {
        return Result.success();
    }
}

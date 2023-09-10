package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sky.baidumap")
public class BaiduMapProperties {
    // 百度地图访问应用AK
    private String ak;
    // 用户的权限签名
    private String sn;
    // 地理编码服务请求地址
    private String webservice_geocoding_url;
    // 轻量级路线规划服务请求地址,为骑手规划路线，默认请求骑行方式路线
    private String webservice_DirectionLite_url;
}

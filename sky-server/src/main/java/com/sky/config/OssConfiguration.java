package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 * @author paxi
 * @data 2023/8/27
 **/
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * 创建AliOssUtil类
     * @param aliOssProperties 加载阿里云OSS配置的类
     * @return com.sky.utils.AliOssUtil
     * @author paxi
     * @data 2023/8/27
     **/
    @Bean
    // 要是有工具类被创建，就不要再创建了，全局一般对于工具类只需一个
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}

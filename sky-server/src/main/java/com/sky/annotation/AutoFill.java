package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要公共字段填充处理
 * @author paxi
 * @data 2023/8/26
 **/
// 指定注解标识的位置，这里为指定注解只能添加到方法上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 通过枚举指定数据库操作类型 insert update
    OperationType value();
}

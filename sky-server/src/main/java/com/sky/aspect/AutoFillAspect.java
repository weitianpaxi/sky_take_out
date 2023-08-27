package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段填充
 * @author paxi
 * @data 2023/8/26
 **/
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点,
     * 其表达式描述为拦截mapper包下的所有类以及方法，方法的参数是任意的，并且是添加了自定义的@AutoFill注解的方法
     * 即给上述规则匹配到的方法增加功能å
     * @author paxi
     * @data 2023/8/26
     **/
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){
    }

    /**
     * 前置通知，在通知中进行公共字段赋值
     * @return void
     * @author paxi
     * @data 2023/8/26
     **/
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取到当前被拦截的方法上数据库操作类型
        // 方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获得方法上的注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取注解对象中的值，即数据库操作类型
        OperationType operationType = autoFill.value();

        // 获取到被拦截的方法的参数--实体对象
        // 拿到方法的所有参数
        Object[] args = joinPoint.getArgs();
        if (args == null || 0 == args.length) {
            return;
        }
        // 约定更新，新增操作的第一个参数为实体类对象
        Object entity = args[0];

        // 准备赋值的数据
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当前操作用户ID
        Long id = BaseContext.getCurrentId();

        // 根据当前不同的操作类型，为实体对象的公共字段填充赋值
        if (operationType == OperationType.INSERT) {
            // 为四个公共字段赋值
            try {
                // 通过反射，获取实体类中的set方法
                Method setCreatTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreatUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射赋值
                setCreatTime.invoke(entity,now);
                setCreatUser.invoke(entity,id);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE)
        {
            // 为两个公共字段赋值
            try {
                // 通过反射，获取实体类中的set方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

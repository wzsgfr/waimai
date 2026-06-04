package com.sky.aspect;

import com.sky.anno.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component

public class AutoFillAspect {
    @Before("@annotation(com.sky.anno.AutoFill)")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行数据填充");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        OperationType value = annotation.value();
        Object[] args = joinPoint.getArgs();//获取方法参数
        if(args == null || args.length == 0){
            return;
        }
        Object entity =args[0];
        try {
            if(value == OperationType.INSERT){

                   Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                   Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                   Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                   Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                   setCreateTime.invoke(entity, LocalDateTime.now());
                   setUpdateTime.invoke(entity, LocalDateTime.now());
                   setCreateUser.invoke(entity, BaseContext.getCurrentId());
                   setUpdateUser.invoke(entity, BaseContext.getCurrentId());
            }else if(value == OperationType.UPDATE){
                   Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                   Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

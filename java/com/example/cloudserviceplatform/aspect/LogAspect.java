package com.example.cloudserviceplatform.aspect;

import com.example.cloudserviceplatform.annotation.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * 日志切面
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    /**
     * 日志切点，匹配使用Log注解的方法
     */
    @Pointcut("@annotation(com.example.cloudserviceplatform.annotation.Log)")
    public void logPointcut() {
        // 切点表达式定义
    }

    /**
     * 环绕通知，处理Log注解
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = null;
        
        // 获取方法签名和注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        
        // 构建日志消息
        String className = point.getTarget().getClass().getName();
        String methodName = method.getName();
        String description = logAnnotation != null && logAnnotation.value() != null && !logAnnotation.value().isEmpty() 
                ? logAnnotation.value() : "无描述";
        
        try {
            // 发送方法开始日志到RabbitMQ
            String startLogMessage = String.format("===== 开始执行方法: %s.%s ===== 描述: %s", 
                    className, methodName, description);
            
            // 记录请求参数（如果需要）
            if (logAnnotation != null && logAnnotation.logParams()) {
                String paramMessage = String.format("方法: %s.%s 请求参数: %s", 
                        className, methodName, Arrays.toString(point.getArgs()));
                logger.info(paramMessage);
            }
            
            // 执行方法
            result = point.proceed();
            
            // 记录返回结果（如果需要）
            if (logAnnotation != null && logAnnotation.logResult()) {
                String resultMessage = String.format("方法: %s.%s 返回结果: %s", 
                        className, methodName, result);
                logger.info(resultMessage);
            }
            
            return result;
        } catch (Exception e) {
            // 记录异常日志
            String errorLogMessage = String.format("===== 方法执行失败: %s.%s ===== 异常: %s", 
                    className, methodName, e.getMessage());
            logger.error(errorLogMessage, e);
            throw e;
        } finally {
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - beginTime;
            
            String endLogMessage = String.format("===== 方法执行完成: %s.%s ===== 耗时: %d ms", 
                    className, methodName, executionTime);
            logger.info(endLogMessage);
        }
    }
}
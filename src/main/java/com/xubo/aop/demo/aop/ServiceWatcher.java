package com.xubo.aop.demo.aop;

import com.xubo.aop.demo.annotation.TestServiceWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @Author xubo
 * @Date 2022/3/1 16:47
 * 切面类
 */
@Aspect
@Component("serviceWatcher")
public class ServiceWatcher {

    // 日志在实际项目中采用日志框架 log4j  log.info()
    Logger logger = Logger.getLogger("AppServiceWatcherLogger");

    @Around("@annotation(com.xubo.aop.demo.annotation.TestServiceWatch)")
    public Object around(ProceedingJoinPoint joinPoint) {
        if(!isParamsOpen()) {
            return null;
        }
        String name = null;
        name = getName(joinPoint);

        String func = null;
        func = getFunc(joinPoint);

        return callFunctionAndMarkResult(joinPoint, getFuncName(joinPoint), name, func);
    }

    /**
     * 做埋点操作，并打印日志
     * @param joinPoint
     * @param funcName
     * @param name
     * @param func
     * @return
     */
    protected Object callFunctionAndMarkResult(ProceedingJoinPoint joinPoint, String funcName, String name, String func) {
        long start = System.currentTimeMillis();
        try {
            Object resultObject = joinPoint.proceed();
            Map<String, Object> paramMap = isParamsOpen() ? getParamsMap(joinPoint.getArgs(), resultObject) : null;
            logger.info(name +  " " + func + " " + LocalDateTime.now() + " " + paramMap.toString());
            return resultObject;
        } catch (Throwable throwable) {
            Map<String, Object> paramMap = isParamsOpen() ? getParamsMap(joinPoint.getArgs(), null
            ) : null;
            logger.warning(name + " " + func + " " + LocalDateTime.now() + " " + paramMap.toString());
            throwable.printStackTrace();
        }finally {

        }
        return null;
    }

    protected String getFuncName(ProceedingJoinPoint joinPoint) {
        String className = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return className + "." + ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
    }

    /**
     * 获取关键入参，用于记录在日志中
     * @param args
     * @param resultObject
     * @return
     */
    protected Map<String, Object> getParamsMap(Object args, Object resultObject) {
        Map<String, Object> paramsMap = new HashMap<>(16);
        try {
            paramsMap.put("input params", args == null ? "null" : args);
            paramsMap.put("result", resultObject == null ? "null" : resultObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsMap;
    }

    /**
     * 是否开启日志记录，可以通过配置文件的方式赋值
     * @return
     */
    protected boolean isParamsOpen() {
        // 获取配置文件 这里直接给true,后续需要手动实现
        return true;
    }

    /**
     * 获取注解上的对象
     * @param joinPoint
     * @return
     */
    protected String getName(ProceedingJoinPoint joinPoint) {
        try {
            // 获取注解括号里面的参数
            return ((TestServiceWatch)getAnnotation(joinPoint, TestServiceWatch.class)).annoArg0();
        } catch (NoSuchMethodException e) {
            return "";
        }
    }

    /**
     * 获取切点注解
     * @param joinPoint
     * @param annotation
     * @return
     */
    protected Object getAnnotation(ProceedingJoinPoint joinPoint, Class annotation) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> classTarget = joinPoint.getSignature().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 获取使用该注解修饰的方法
        Method objMethod = classTarget.getMethod(methodName, par);
        // 获取方法上的注解
        return objMethod.getAnnotation(annotation);
    }

    /**
     * 获取注解中的参数
     * @param joinPoint
     * @return
     */
    protected String getFunc(ProceedingJoinPoint joinPoint) {
        try {
            return ((TestServiceWatch)getAnnotation(joinPoint, TestServiceWatch.class)).annoArg1();
        } catch (NoSuchMethodException e) {
            return "";
        }
    }
}

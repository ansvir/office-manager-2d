package com.tohant.om2d.storage.cache;

import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.storage.cache.annotation.Cached;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class CachedAspect {

    private final GameCache cacheService;

    @Around("@annotation(com.tohant.om2d.storage.cache.annotation.Cached)")
    public Object cacheMethodResult(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String cacheName = methodSignature.getMethod().getAnnotation(Cached.class).cacheName();
        String cacheKey = generateCacheKey(cacheName, joinPoint.getArgs());

        Object cachedResult = cacheService.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        Object result = joinPoint.proceed();
        cacheService.put(cacheKey, result);

        return result;
    }

    private String generateCacheKey(String cacheName, Object[] args) {
        return cacheName + Arrays.hashCode(args);
    }

}
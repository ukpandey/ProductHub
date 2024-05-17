package com.example.ProductHub;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MyProductsConrollerLoggingAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyProductsConrollerLoggingAspect.class);
	
	@Before("execution(* com.example.ProductHub.Controller.MyProductsController.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        LOGGER.info("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.example.ProductHub.Controller.MyProductsController.*(..))", returning = "result")
    public void logAfterReturningMethod(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exiting method: " + joinPoint.getSignature().getName() + " with result: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.ProductHub.Controller.MyProductsController.*(..))", throwing = "exception")
    public void logAfterThrowingMethod(JoinPoint joinPoint, Throwable exception) {
        LOGGER.info("Exception in method: " + joinPoint.getSignature().getName() + " with message: " + exception.getMessage());
    }

}

package com.example.logisticms.config;


import com.google.common.flogger.FluentLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    // Initialize Flogger
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    /**
     * Pointcut that matches all repositories, services, and REST controllers.
     * Adjust the package name to match your project structure.
     */
    @Pointcut("within(com.example.logisticms..*) && " +
            "(within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Service *) || " +
            "within(@org.springframework.stereotype.Repository *))")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();

        // Log Method Entry
        logger.atInfo().log("Enter: %s.%s() with argument[s] = %s",
                className, methodName, Arrays.toString(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();

            stopWatch.stop();

            // Log Method Exit & Execution Time
            logger.atInfo().log("Exit: %s.%s() with result = %s. Execution time: %d ms",
                    className, methodName, result, stopWatch.getTotalTimeMillis());

            return result;
        } catch (Exception e) {
            // Log Errors
            logger.atSevere().withCause(e).log("Exception in %s.%s() with cause = %s",
                    className, methodName, e.getMessage() != null ? e.getMessage() : "NULL");
            throw e;
        }
    }
}

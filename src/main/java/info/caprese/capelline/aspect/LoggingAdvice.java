package info.caprese.capelline.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    @Before("execution(* info.caprese.capelline.controller.*.*(..))")
    public void invokeApiControllerBefore(JoinPoint joinPoint) {
        outputLog(joinPoint);
    }


    @AfterReturning(pointcut = "execution(* info.caprese.capelline.controller.*.*(..))", returning = "returnValue")
    public void invokeControllerAfter(JoinPoint joinPoint, Object returnValue) {
        outputLog(joinPoint, returnValue);
    }

    @AfterThrowing(value = "execution(* info.caprese.capelline.controller.*.*(..))", throwing = "e")
    public void invokeControllerAfterThrowing(JoinPoint joinPoint, Throwable e) {
        outputErrorLog(joinPoint, e);
    }


    private void outputLog(JoinPoint joinPoint) {
        String logMessage = getClassName(joinPoint) + "." + getSignatureName(joinPoint) + " [開始] - " + getArguments(joinPoint);
        log.info(logMessage);
    }

    private void outputLog(JoinPoint joinPoint, Object returnValue) {
        String logMessage = getClassName(joinPoint) + "." + getSignatureName(joinPoint) + " [終了] - " + getReturnValue(returnValue);
        log.info(logMessage);
    }

    private void outputErrorLog(JoinPoint joinPoint, Throwable e) {
        String logMessage = "[" + getClassName(joinPoint) + "." + getSignatureName(joinPoint) + "] - " + getArguments(joinPoint);
        log.error(logMessage, e);
    }


    private String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName();
    }

    private String getSignatureName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
    private String getArguments(JoinPoint joinPoint) {
        if (joinPoint.getArgs() == null) {
            return "argument is null";
        }

        Object[] arguments = joinPoint.getArgs();
        ArrayList<String> argumentStrings = new ArrayList();

        for (Object argument : arguments) {
            if (argument != null) {
                argumentStrings.add(argument.toString());
            }
        }
        return String.join(",", argumentStrings);
    }

    private String getReturnValue(Object returnValue) {
        return (returnValue != null) ? returnValue.toString() : "return value is null";
    }

}

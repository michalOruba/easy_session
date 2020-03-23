package com.michaloruba.obslugasesji.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Pointcut("execution(* com.michaloruba.obslugasesji.controller.*.save*(..))")
    private void forControllerSavePackage(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.service.*.save*(..))")
    private void forServiceSavePackage(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.dao.*.save*(..))")
    private void forDaoSavePackage(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.controller.*.delete*(..))")
    private void forControllerDeletePackage(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.service.*.delete*(..))")
    private void forServiceDeletePackage(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.dao.*.delete*(..))")
    private void forDaoDeletePackage(){}

    @Pointcut("forControllerSavePackage() || forServiceSavePackage() || forDaoSavePackage()")
    private void forSaveFlow(){}

    @Pointcut("forControllerDeletePackage() || forServiceDeletePackage() || forDaoDeletePackage()")
    private void forDeleteFlow(){}

    @Before("forSaveFlow()")
    public void beforeSaveFlow(JoinPoint joinPoint){
        logBasicData(joinPoint, "@Before");
        logArguments(joinPoint);
    }

    @AfterReturning(pointcut = "forSaveFlow()")
    public void afterReturningSave(JoinPoint joinPoint){
        logBasicData(joinPoint, "@AfterReturning");
    }

    @AfterThrowing(pointcut = "forSaveFlow()", throwing = "exception")
    public void afterThrowingSave(JoinPoint joinPoint, Throwable exception){
        logBasicData(joinPoint, "@AfterThrowing");
        logException(exception);
    }

    @Before("forDeleteFlow()")
    public void beforeDelete(JoinPoint joinPoint){
        logBasicData(joinPoint, "@Before");
        logArguments(joinPoint);
    }

    @AfterThrowing(pointcut = "forDeleteFlow()", throwing = "exception")
    public void afterThrowingDelete(JoinPoint joinPoint, Throwable exception){
        logBasicData(joinPoint, "@AfterThrowing");
        logException(exception);
    }

    private void logBasicData(JoinPoint joinPoint, String adviceType){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        logger.info("{}: The method called is: {}, User calling method: {}", adviceType, joinPoint.getSignature().toShortString(), currentUserName);
    }

    private void logArguments(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        for(Object tempArg : args){
            logger.info("The argument is: " + tempArg);
        }
    }

    private void logException(Throwable exception){
        logger.warn("The exception is: {}", exception.getMessage());
    }
}

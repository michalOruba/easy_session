package com.michaloruba.obslugasesji.aop;

import com.michaloruba.obslugasesji.user.CrmUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SecurityAspect {

    Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Pointcut("execution(* org.springframework.security.web.authentication.AuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    private void forAuthSuccessHandler(){}

    @Pointcut("execution(* org.springframework.security.web.authentication.AuthenticationFailureHandler.*(..))")
    private void forAuthFailureHandler(){}

    @Pointcut("execution( * com.michaloruba.obslugasesji.config.CustomAuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    private void forCustomAuthSuccessHandler(){}

    @Pointcut("execution(* org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler.onLogoutSuccess(..))")
    private void forLogoutSuccessHandler(){}

    @Pointcut("forAuthFailureHandler() || forAuthSuccessHandler() || forCustomAuthSuccessHandler() || forLogoutSuccessHandler()")
    private void forLoginFlow(){}

    @Pointcut("execution(* com.michaloruba.obslugasesji.controller.RegistrationController.processRegistrationForm(..))")
    private void forUserRegistration(){}

    @Before("forLoginFlow()")
    public void checkLoginProcess(JoinPoint joinPoint){
        logger.info("METHOD_SIGNATURE " + joinPoint.getSignature().toShortString());
        Object [] args = joinPoint.getArgs();

        for (Object arg : args){

            if (arg instanceof Authentication){
                Authentication auth = (Authentication) arg;

                logger.info("AUTHENTICATION: Username: {}, Roles: {}, Is Authenticated: {}", auth.getName(), auth.getAuthorities(), auth.isAuthenticated());
            }
            else if(arg instanceof HttpServletRequest){
                HttpServletRequest req = (HttpServletRequest) arg;

                logger.info(
                        "REQUEST DATA: Username: {}, Requested URI: {}, Requested URL: {}, Method used: {}, Remote address: {}, Local address: {}",
                        req.getParameter("username"), req.getRequestURI(), req.getRequestURL(), req.getMethod(), req.getRemoteAddr(), req.getLocalAddr()
                        );
            }
        }
    }

    @Before("forUserRegistration()")
    public void checkRegistrationProcess(JoinPoint joinPoint){
        logger.info("METHOD_SIGNATURE " + joinPoint.getSignature().toShortString());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Object [] args = joinPoint.getArgs();

        for (Object arg : args) {

            if (arg instanceof CrmUser) {
                CrmUser user = (CrmUser) arg;

                logger.info("Registered UserName: {}, request IP address: {}, Requested URI: {}, Requested URL: {}", user.getUserName(), request.getRemoteAddr(), request.getRequestURI(), request.getRequestURL());
            }
        }


    }
}

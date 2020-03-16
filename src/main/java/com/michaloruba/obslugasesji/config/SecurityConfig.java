package com.michaloruba.obslugasesji.config;

import com.michaloruba.obslugasesji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    private UserService userService;

    //@Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    //@Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    public SecurityConfig(UserService userService, CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomLogoutSuccessHandler customLogoutSuccessHandler){
        this.userService = userService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/register/*").permitAll()
                .antMatchers("/").hasAnyRole("STUDENT", "ADMIN", "EMPLOYEE", "OWNER")
                .antMatchers("/*/list").hasAnyRole("STUDENT", "ADMIN", "EMPLOYEE", "OWNER")
                .antMatchers("/*/showSessionDetails").hasAnyRole("STUDENT", "ADMIN", "EMPLOYEE", "OWNER")
                .antMatchers("/grades/save*").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE")
                .antMatchers("/grades/show*").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE")
                .antMatchers("/*/show*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/*/save*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/*/delete*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/students/search").hasAnyRole("STUDENT", "ADMIN", "EMPLOYEE", "OWNER")
                .antMatchers( HttpMethod.GET, "/api/*").hasAnyRole("STUDENT", "ADMIN", "EMPLOYEE", "OWNER")
                .antMatchers( HttpMethod.POST, "/api/*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers( HttpMethod.PUT, "/api/*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers( HttpMethod.DELETE, "/api/*").hasAnyRole("ADMIN", "OWNER")
                .and()
                .formLogin()
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}

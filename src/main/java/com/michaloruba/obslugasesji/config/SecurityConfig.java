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

    private UserService userService;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
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
                .antMatchers("/").authenticated()
                .antMatchers("/*/list").authenticated()
                .antMatchers("/*/showSessionDetails").authenticated()
                .antMatchers("/students/search").authenticated()
                .antMatchers("/grades/save*").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE")
                .antMatchers("/grades/show*").hasAnyRole("ADMIN", "OWNER", "EMPLOYEE")
                .antMatchers("/*/show*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/*/save*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/*/delete*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/admin/*/save*").hasRole("ADMIN")
                .antMatchers("/admin/*/delete*").hasRole("ADMIN")
                .antMatchers("/admin/users/showFormForUpdateRoles").hasAnyRole("ADMIN", "OWNER")
                .antMatchers("/admin/*/show*").hasRole("ADMIN")
                .antMatchers("/admin/*/*").hasAnyRole("ADMIN", "OWNER")
                .antMatchers( HttpMethod.GET, "/api/*").authenticated()
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

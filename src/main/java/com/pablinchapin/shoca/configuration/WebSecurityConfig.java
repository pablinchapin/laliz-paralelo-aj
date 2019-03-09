/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.configuration;

import com.pablinchapin.shoca.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author pvargas
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public WebSecurityConfig() {
        super();
    }
    
    
    
    
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        
    return bCryptPasswordEncoder;    
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        System.out.println("public void configureGlobal-> "+auth);
        //Setting service to find user in the database and setting PasswordEncoder
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        
        System.out.println("protected void configure(HttpSecurity http)");
        
        http.csrf().disable();
        
        //Requires login with role ROLE_EMPLOYEE or ROLE_MANAGER
        //If not, it will redirect to /admin/login
        http.authorizeRequests()
                .antMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo")
                .access("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')");
        
        //Pages only for ROLE_MANAGER
        http.authorizeRequests()
                .antMatchers("/admin/product")
                .access("hasRole('ROLE_MANAGER')");
        
        //When user login, role XX, but access to the page requires the YY role,
        //An AccessDeniedException will be thrown
        http.authorizeRequests()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");
        
        //Configuration for Login Form
        //.loginPage("/admin/login")
        http.authorizeRequests()
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")//Submit URL
                .loginPage("/admin/login")
                .defaultSuccessUrl("/admin/accountInfo")
                .failureUrl("/admin/login?error=true")
                .usernameParameter("userName")
                .passwordParameter("password")
                //Configuration for the Logout page
                //After logout, got to home page
                .and()
                .logout()
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/");
                
    }
    
}

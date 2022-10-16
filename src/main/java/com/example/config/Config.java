package com.example.config;

import com.example.config.filter.CustomAuthFilter;
import com.example.config.filter.CustomAuthorizationFilter;
import com.example.service.UserService;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMongock
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config  extends WebSecurityConfigurerAdapter  {
    @Autowired
    @Lazy
    private  UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var filter = customAuthFilter();
        filter.setFilterProcessesUrl("/auth/login");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/blogs/**","/users/**").access("not( hasRole('ROLE_REFRESH') )")
                .antMatchers("/blogs/**","/users/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/auth/login","/auth/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(filter)
                .addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        var auth=new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public CustomAuthFilter customAuthFilter() throws Exception {
        return new CustomAuthFilter(authenticationManagerBean());
    }
    @Bean
    public CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter();
    }


    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/**/v2/api-docs",
                "/**/swagger-resources/**",
                "/**/swagger-ui.html",
                "/**/webjars/**");
    }

}

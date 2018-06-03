package com.parking.Parking.config;

import com.parking.Parking.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Environment env;
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    CustomSuccessHandler customSuccessHandler;

    private static final String SALT = "salt";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/",
            "/error/**/*",
            "/signup"
    };

    private static final String[] ADMIN_MATCHERS = {
            "/drivers/**",
            "/parking",
            "/transactions",
    };

    private static final String[] USER_MATCHERS = {
            "/driver/**",
            "/transaction",
    };

    private static final String[] OWNER_MATCHERS = {
            "/income"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .antMatchers(ADMIN_MATCHERS).access("hasRole('ROLE_ADMIN')")
            .antMatchers(USER_MATCHERS).access("hasRole('ROLE_USER')")
            .antMatchers(OWNER_MATCHERS).access("hasRole('ROLE_OWNER')")
            .anyRequest()
            .authenticated();

        http
            .csrf().disable().cors().disable()
            .formLogin().failureUrl("/index?error").loginPage("/login").successHandler(customSuccessHandler).permitAll()
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").deleteCookies("remember-me").permitAll()
            .and()
            .rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }

}

package com.divum.hiring_platform.config;


import com.divum.hiring_platform.util.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private static final String ADMIN = "admin";
    private static final String EMPLOYEE = "employee";

    private final CorsConfig corsConfig;
    private static final String[] ADMIN_ENDPOINTS = {
            "/api/v1/employee",
            "/api/v1/employee/{id}",
            "/api/v1/employee",
            "/api/v1/employee/interview/{interviewId}",
            "/api/v1/excel/**",
            "/api/v1/final",
            "/api/v1/questions/mcq/**",
            "/api/v1/result/{contestId}/{userId}",
            "/api/v1/contest/**",
            "/api/v1/contest/round**",
    };

    private static final String[] ADMIN_AND_EMPLOYEE_ENDPOINTS = {
            "/api/v1/employee/password-reset/email/{emailId}",
            "/api/v1/employee/{employeeId}/interviews/{contestId}",
            "/api/v1/employee/{interviewId}/interviewRequestType/{interviewRequestType}",
            "/api/v1/employee/password-reset",
            "/api/v1/employee/contests/{employeeId}",
            "/api/v1/employee/interview/{interviewId}",
            "/api/v1/employee/contests/log/{employeeId}"
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("api/v1/auth/login").permitAll();
                    authorizeRequests.requestMatchers(ADMIN_ENDPOINTS).hasAuthority(ADMIN);
                    authorizeRequests.requestMatchers(ADMIN_AND_EMPLOYEE_ENDPOINTS).hasAnyAuthority(ADMIN, EMPLOYEE);
                    authorizeRequests.anyRequest().authenticated();
                })
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }



}



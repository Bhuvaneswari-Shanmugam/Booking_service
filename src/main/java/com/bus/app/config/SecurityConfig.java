package com.bus.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/booking/create").permitAll()
                        .requestMatchers("/booking/cancel").permitAll()
                        .requestMatchers("/booking/edit").permitAll()
                        .requestMatchers("/booking/retrieve/by-user").permitAll()
                        .requestMatchers("/booking/user/all").permitAll()
                        .requestMatchers("passenger/create").permitAll()
                        .requestMatchers("passenger/user/{userId}").permitAll()
                        .requestMatchers("passenger/create").permitAll()
                        .requestMatchers("passenger/retrieve").permitAll()
                        .requestMatchers("/booking/retrieve").hasRole("ADMIN")
                        .requestMatchers("passenger/retrieve/all-passenger").hasRole("ADMIN")
                        .requestMatchers("ticket/retrieve/all-ticket").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

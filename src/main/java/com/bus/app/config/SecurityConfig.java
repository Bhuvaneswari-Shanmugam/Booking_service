package com.bus.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/booking/create").permitAll()
                        .requestMatchers("/passenger/create").permitAll()
                        .requestMatchers("/ticket/retrieve/bookings").permitAll()
                        .requestMatchers("/booking/cancel").permitAll()
                        .requestMatchers("/ticket/create").permitAll()
                        .requestMatchers("/booking/retrieve/past-booking").permitAll()
                        .requestMatchers("/booking/retrieve/upcoming-booking").permitAll()
                        .requestMatchers("/booking/retrieve/ticketId").permitAll()
                        .requestMatchers("/booking/cancel-ticket").permitAll()
                        .requestMatchers("/booking/retrieve").hasRole("ADMIN")
                        .requestMatchers("/passenger/retrieve/gender/seat-list").permitAll()
                        .requestMatchers("/passenger/retrieve/all-passenger").hasRole("ADMIN")
                        .requestMatchers("/ticket/retrieve/ticket").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

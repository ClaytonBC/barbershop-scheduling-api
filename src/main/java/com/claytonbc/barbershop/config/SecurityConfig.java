package com.claytonbc.barbershop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()

                        // CUSTOMERS
                        .requestMatchers(HttpMethod.GET, "/customers").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/customers/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/customers/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/services").permitAll()
                        .requestMatchers(HttpMethod.POST, "/services").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/services/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/services/**").hasRole("OWNER")

                        .requestMatchers(HttpMethod.GET, "/appointments")
                        .hasAnyRole("CLIENT", "OWNER")

                        .requestMatchers(HttpMethod.GET, "/appointments/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.POST, "/appointments")
                        .hasRole("CLIENT")

                        .requestMatchers(HttpMethod.PATCH, "/appointments/**")
                        .hasRole("OWNER")

                        .requestMatchers(HttpMethod.DELETE, "/appointments/**")
                        .authenticated()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

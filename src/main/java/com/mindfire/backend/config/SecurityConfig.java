package com.mindfire.backend.config;

import com.mindfire.backend.security.CustomUserDetailsService;
import com.mindfire.backend.security.JwtAuthenticationEntryPoint;
import com.mindfire.backend.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/login").permitAll()
                        .requestMatchers("/api/v1/user-service/user/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/v1/user-service/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException)
                                -> response.setStatus(HttpServletResponse.SC_FORBIDDEN)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

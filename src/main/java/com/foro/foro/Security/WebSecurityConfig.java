package com.foro.foro.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {



    private static final String[] ALLOWED_ORIGINS = {
            "http://localhost:8080",
            "http://localhost:4200",
            "http://172.16.77.34:3000",
            "http://localhost:5173",
            "http://localhost:19006",
            "http://localhost",
            "http://172.16.77.34:19006",
            "http://172.16.77.34",
            "http://localhost:3000",
            "http://loginfront:3000",
            "http://loginfront:80",
    };


    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    @Transactional(readOnly = true)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    // Permitir acceso sin autenticación para rutas específicas
                    authorize.requestMatchers("/auth/**", "/public/**").permitAll();



                    // Configuración estática de rutas protegidas
                    authorize.requestMatchers(new AntPathRequestMatcher("/api/topic/{id}", "GET"))
                            .hasAnyAuthority("USER");
                    authorize.requestMatchers(new AntPathRequestMatcher("/api/topic", "GET"))
                            .hasAnyAuthority("USER");
                    authorize.requestMatchers(new AntPathRequestMatcher("/api/topic", "POST"))
                            .hasAnyAuthority("USER");
                    authorize.requestMatchers(new AntPathRequestMatcher("/api/topic/{id}", "PUT"))
                            .hasAnyAuthority("USER");
                    authorize.requestMatchers(new AntPathRequestMatcher("/api/topic/{id}", "DELETE"))
                            .hasAnyAuthority("USER");


                    // Requerir autenticación para cualquier otra ruta
                    authorize.anyRequest().denyAll();
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*", "Authorization")
                        .allowedOrigins(ALLOWED_ORIGINS) // Explicitly list allowed origins
                        .allowedMethods("*")
                        .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}

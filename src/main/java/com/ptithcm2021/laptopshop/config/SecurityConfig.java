package com.ptithcm2021.laptopshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtConfig jwtConfig;

    public SecurityConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/create").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/promotions/product-detail/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/promotions/shop-promotion/is-active").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/series/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                .requestMatchers("/api/payment-callback/**").permitAll()
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/**" ).permitAll()
                .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2Config -> oauth2Config
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtConfig))
                .authenticationEntryPoint(new AuthenticationEntryPoint())
        );
        http.cors(cors -> cors.configurationSource(request ->{
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of(
                    "http://localhost:8080",
                    "http://localhost:3000",
                    "https://dev.api.mylaptopshop.me",
                    "http://dev.api.mylaptopshop.me",
                    "http://localhost:5173",
                    "http://localhost:5501",
                    "http://localhost:5500",
                    "http://127.0.0.1:5501",
                    "https://laptop-store-client-ashen.vercel.app",
                    "https://www.mylaptopshop.me"
                    ));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS", "PUT"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }));

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

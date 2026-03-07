package com.app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//허용 도메인 목록
	private final List<String> ALLOWED = List.of("https://vue-front-807ffc6b572e.herokuapp.com"
											   , "https://guney.kro.kr"
                                               , "chrome-extension://ilaafkabnbkdgaieedckdnfkmapocimc"
                                               , "chrome-extension://chjbcemdkiommdpeklplkbfpemefejcp"
//                                               , "http://localhost"
                                               );
    
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))	//콜스콜스
            .authorizeHttpRequests(authorize -> authorize
        		.requestMatchers("/v3/api-docs/**").permitAll()
        		.requestMatchers("/swagger-ui/**").permitAll()
              	.requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/api/private/**").authenticated()				//허용되지 않은 출처의 요청이나 토큰 없는 요청은 여기서 손절(나중에 인증된 사용자만 접근 가능하게 수정해야함)
                .anyRequest().permitAll()
            ).addFilterBefore((request, response, chain) -> {					//필터체인 적용
                HttpServletRequest req = (HttpServletRequest) request;

                String requestUri = req.getRequestURI();
                String origin = req.getHeader("Origin");
                
                Boolean isSwagger = requestUri.startsWith("/swagger-ui") || 
                        requestUri.startsWith("/v3/api-docs") ||
                        requestUri.startsWith("/swagger-resources");

                //만약 Origin이 있는데 허용되지 않은 경우라면 잘가시고, 스웨거는 통과
                if (isSwagger.equals(Boolean.FALSE) && origin != null && !ALLOWED.contains(origin)) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                
                chain.doFilter(request, response);
            }, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(ALLOWED);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); //쿠키나 인증 정보를 포함할 경우 필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

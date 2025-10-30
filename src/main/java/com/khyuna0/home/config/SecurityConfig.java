package com.khyuna0.home.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.khyuna0.home.jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // csrf 비활성화
			.authorizeHttpRequests(auth -> auth 
					.requestMatchers("/api/auth/**").permitAll() // 인증 없이 접근 가능한 요청들
					.anyRequest().authenticated())
			
					.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
					// 로그인하지 않고도 JWT 존재하면 요청 받게 하는 설정 
					
					.cors(cors -> cors.configurationSource(request -> {
						CorsConfiguration configuration = new CorsConfiguration();
						configuration.setAllowCredentials(true);
						configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://spring-webserver-cloudfront-s3.s3-website.ap-northeast-2.amazonaws.com")); // 허용 ip 주소
						configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
						configuration.setAllowedHeaders(List.of("*"));
						return configuration;
					})
				);
		
		return http.build();
	}	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
		// 사용자 인증을 처리하는 객체 반환
	}
}

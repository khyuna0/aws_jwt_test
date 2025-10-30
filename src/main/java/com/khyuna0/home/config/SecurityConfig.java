package com.khyuna0.home.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	
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
					
					// 로그인
					.formLogin(form -> form 
							.loginProcessingUrl("/api/auth/login") // 로그인 처리 요청
							.defaultSuccessUrl("/api/auth/apicheck", true) // 로그인 성공 시 이동할 url
							.usernameParameter("username")
			               .passwordParameter("password")
			               .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
			               .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
							.permitAll())
					
					// 로그아웃
					.logout(logout -> logout 
							.logoutUrl("/api/auth/logout")
							.logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
							.permitAll())
					
					.cors(cors -> cors.configurationSource(request -> {
						CorsConfiguration configuration = new CorsConfiguration();
						configuration.setAllowCredentials(true);
						configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://spring-webserver-cloudfront-s3.s3-website.ap-northeast-2.amazonaws.com")); // 허용 ip 주소
						configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
						configuration.setAllowedHeaders(List.of("*"));
						return configuration;
					}));
		return http.build();
	}		
}

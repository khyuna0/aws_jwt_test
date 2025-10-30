package com.khyuna0.home.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret; // application.properties 내에 선언되어 있는 jwt.secret 값 가져오기
	
	@Value("${jwt.expiration}")
	private Long expiration; // 토큰의 유효시간
	
	// 토큰 생성
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username) // 인증 받을 사용자 이름
				.setIssuedAt(new Date()) // 토큰 발급 시간
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간
				.signWith(SignatureAlgorithm.HS256 , secret)
				.compact();
	}
	
	// 토큰에서 username 추출 메서드 -> 로그인 후에 받은 JWT를 검증 -> 누구의 토큰인지 확인하는 메서드
	public String extractUsername(String token) {
		return Jwts.parser() // 토큰을 파싱해주는 parser 생성
				.setSigningKey(secret) // 서명이 맞는지 검증용 비밀키 설정
				.parseClaimsJws(token) // 토큰 문자열 분석 -> 서명이 맞는지 검증
				.getBody() //payload 부분 가져옴
				.getSubject(); // 사용자 이름 추출 (username)
	}
	
}

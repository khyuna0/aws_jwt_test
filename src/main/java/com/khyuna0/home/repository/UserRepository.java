package com.khyuna0.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khyuna0.home.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByUsername(String username);
}

package com.matheuslt.booklibrary.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matheuslt.booklibrary.models.User;
import com.matheuslt.booklibrary.repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public Optional<User> findByEmail(String email) {
		return repository.findByEmail(email);
	}
}

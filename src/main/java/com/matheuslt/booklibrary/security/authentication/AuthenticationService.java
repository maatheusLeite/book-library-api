package com.matheuslt.booklibrary.security.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.matheuslt.booklibrary.models.User;
import com.matheuslt.booklibrary.models.enums.Role;
import com.matheuslt.booklibrary.repositories.UserRepository;
import com.matheuslt.booklibrary.security.config.JwtService;

@Service
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder,
				JwtService jwtService, AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationResponse registerUser(RegisterRequest request) {
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		
		repository.save(user);
		
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}
	
	public AuthenticationResponse registerAdmin(RegisterRequest request) {
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.ADMIN);
		
		repository.save(user);
		
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		User user = repository.findByEmail(request.getEmail()).orElseThrow();
		
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}
}

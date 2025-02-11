package com.uploadservice.controller;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uploadservice.exception.ResourceNotFoundException;
import com.uploadservice.payloads.JwtAuthRequest;
import com.uploadservice.payloads.JwtAuthResponse;
import com.uploadservice.security.JWTUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    private final AuthenticationManager authenticationManager;
    private final JWTUtility jwtUtil;
    private final UserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager, JWTUtility jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody JwtAuthRequest request) {
    	try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    		String token = jwtUtil.generateToken(userDetails);
    		
    		if (nonNull(token))
    			return new ResponseEntity<>(new JwtAuthResponse(true, token), OK);
    		return new ResponseEntity<>(new JwtAuthResponse(false, null), NO_CONTENT);			
		} catch (Exception e) {
			logger.error("Error Occured while login.. {}", e.getLocalizedMessage());
			throw new ResourceNotFoundException("User", "username & password", request.getUsername() + " & " + request.getPassword());
		}
		
    }
}
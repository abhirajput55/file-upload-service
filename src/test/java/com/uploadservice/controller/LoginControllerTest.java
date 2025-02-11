package com.uploadservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.uploadservice.exception.ResourceNotFoundException;
import com.uploadservice.payloads.JwtAuthRequest;
import com.uploadservice.payloads.JwtAuthResponse;
import com.uploadservice.security.JWTUtility;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtility jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    private JwtAuthRequest validRequest;
    private JwtAuthRequest invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new JwtAuthRequest("admin", "password123");
        invalidRequest = new JwtAuthRequest("admin", "wrongpassword");
    }

    @Test
    void login_SuccessfulAuthentication_ReturnsJwtToken() {
        // Mock authentication process correctly
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(validRequest.getUsername())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mock-jwt-token");

        ResponseEntity<JwtAuthResponse> response = loginController.login(validRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().isStatus());
        assertEquals("mock-jwt-token", response.getBody().getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(validRequest.getUsername());
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }


    @Test
    void login_InvalidCredentials_ThrowsResourceNotFoundException() {
        // Simulate authentication failure
        doThrow(new BadCredentialsException("Bad credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResourceNotFoundException thrownException = null;

        try {
            loginController.login(invalidRequest);
        } catch (ResourceNotFoundException e) {
            thrownException = e;
        }

        assertEquals("User not found with the given username & password : admin & wrongpassword", thrownException.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void login_TokenGenerationFails_ReturnsNoContent() {
        // Mock authentication but return null token
    	when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(validRequest.getUsername())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(null);

        ResponseEntity<JwtAuthResponse> response = loginController.login(validRequest);

        assertEquals(204, response.getStatusCodeValue());
        assertEquals(false, response.getBody().isStatus());
        assertEquals(null, response.getBody().getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(validRequest.getUsername());
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }
}

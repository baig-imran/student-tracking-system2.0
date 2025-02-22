package com.sts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.UserRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationProvider authenticationProvider;

    
    public AuthController(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}


//	@PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
//        Authentication authentication = authenticationProvider.authenticate(
//            new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        return ResponseEntity.ok("Logged in successfully");
//    }
}


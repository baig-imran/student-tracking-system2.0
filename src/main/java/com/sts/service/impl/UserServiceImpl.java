package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sts.dto.UserRequest;
import com.sts.dto.UserResponse;
import com.sts.entity.Users;
import com.sts.exceptions.CustomException;
import com.sts.repository.UserRepository;
import com.sts.service.interfaces.JWTService;
import com.sts.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, 
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, 
                           JWTService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        log.info("Attempting to save new user with username: {}", userRequest.getUserName());

        // Encode the password before saving
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Users newUser = modelMapper.map(userRequest, Users.class);
        Users savedUser = userRepository.save(newUser);

        log.info("User successfully saved with ID: {}", savedUser.getUserName());

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public String verify(UserRequest userRequest) {
        log.info("Verifying user authentication for username: {}", userRequest.getUserName());

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                log.info("User authentication successful for username: {}", userRequest.getUserName());
                return jwtService.generateToken(userRequest.getUserName());
            }
        } catch (Exception e) {
            log.error("Authentication failed for username: {}", userRequest.getUserName());
            throw e;//new CustomException("Invalid username or password", org.springframework.http.HttpStatus.UNAUTHORIZED);
        } 
//        catch (Exception e) {
//            log.error("Unexpected error during authentication for username: {} - {}", userRequest.getUserName(), e.getMessage());
//            throw e;//new CustomException("Unexpected authentication error", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        return "Authentication failed"; // This line should never be reached due to exception handling.
    }

	@Override
	public String getRole(String username) {
		
		return userRepository.findByUserName(username).getRole();
	}
}

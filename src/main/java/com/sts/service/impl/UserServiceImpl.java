package com.sts.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.UserRequest;
import com.sts.dto.UserResponse;
import com.sts.entity.Users;
import com.sts.exceptions.ConflictException;
import com.sts.exceptions.DatabaseException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.exceptions.UnauthorizedException;
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
    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        log.info("Attempting to save new user with username: {}", userRequest.getUsername());

        if (userRepository.findByUsername(userRequest.getUsername()) != null) {
            throw new ConflictException(ErrorMessageEnum.DUPLICATE_USER.getMessage(userRequest.getUsername()));
        }

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Users newUser = modelMapper.map(userRequest, Users.class);
        
        try {
            Users savedUser = userRepository.save(newUser);
            log.info("User successfully saved with username: {}", savedUser.getUsername());
            return modelMapper.map(savedUser, UserResponse.class);
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", e.getMessage());
            throw new DatabaseException(ErrorMessageEnum.DATABASE_ERROR.getMessage());
        }
    }

    @Override
    public Map<String, Object> verify(UserRequest userRequest) {
        log.info("Verifying user authentication for username: {}", userRequest.getUsername());

        Users user = userRepository.findByUsername(userRequest.getUsername());
        if (user == null) {
            log.error("User not found: {}", userRequest.getUsername());
            throw new ResourceNotFoundException(ErrorMessageEnum.INVALID_CREDENTIALS.getMessage());
        }

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            log.error("Incorrect password for username: {}", userRequest.getUsername());
            throw new UnauthorizedException(ErrorMessageEnum.INVALID_CREDENTIALS.getMessage());
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            log.info("User authentication successful for username: {}", userRequest.getUsername());
            String token = jwtService.generateToken(userRequest.getUsername());
            return buildAuthResponse(userRequest.getUsername(), token);
        }

        log.error("Authentication failed for username: {}", userRequest.getUsername());
        throw new UnauthorizedException(ErrorMessageEnum.UNAUTHORIZED_ACCESS.getMessage());
    }

    @Override
    public String getRole(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(ErrorMessageEnum.INVALID_CREDENTIALS.getMessage());
        }
        return user.getRole();
    }

    private Map<String, Object> buildAuthResponse(String username, String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userName", username);
        response.put("role", getRole(username));
        return response;
    }
}

package com.sts.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.user.CreateUserReq;
import com.sts.dto.user.CreateUserRes;
import com.sts.entity.Roles;
import com.sts.entity.Users;
import com.sts.exceptions.ConflictException;
import com.sts.exceptions.DatabaseException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.exceptions.UnauthorizedException;
import com.sts.repository.UserRepository;
import com.sts.service.interfaces.JWTService;
import com.sts.service.interfaces.RolesService;
import com.sts.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RolesService rolesService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           JWTService jwtService, RolesService rolesService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.rolesService = rolesService;
    }

    @Override
    @Transactional
    public CreateUserRes createUser(CreateUserReq createUserReq) {
        log.info("Attempting to save new user with username: {}", createUserReq.getUsername());

        if (userRepository.findByUsername(createUserReq.getUsername()) != null) {
            throw new ConflictException("User already exists with username: " + createUserReq.getUsername());
        }

        // Encode password
        createUserReq.setPassword(passwordEncoder.encode(createUserReq.getPassword()));

        // Map user fields except roles
        Users newUser = modelMapper.map(createUserReq, Users.class);

        // Fetch roles by names or create if missing
        Set<Roles> roles = rolesService.getRolesByNames(createUserReq.getRoles());
        newUser.setRoles(roles);

        try {
            Users savedUser = userRepository.save(newUser);
            log.info("User successfully saved with username: {}", savedUser.getUsername());
            return modelMapper.map(savedUser, CreateUserRes.class);
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", e.getMessage());
            throw new DatabaseException("Failed to save user due to database error");
        }
    }

    @Override
    public Map<String, Object> verify(CreateUserReq createUserReq) {
        log.info("Verifying user authentication for username: {}", createUserReq.getUsername());

        Users user = userRepository.findByUsername(createUserReq.getUsername());
        if (user == null) {
            log.error("User not found: {}", createUserReq.getUsername());
            throw new ResourceNotFoundException("Invalid username or password");
        }

        if (!passwordEncoder.matches(createUserReq.getPassword(), user.getPassword())) {
            log.error("Incorrect password for username: {}", createUserReq.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserReq.getUsername(), createUserReq.getPassword()));

        if (authentication.isAuthenticated()) {
            log.info("User authentication successful for username: {}", createUserReq.getUsername());
            String token = jwtService.generateToken(createUserReq.getUsername());
            return buildAuthResponse(createUserReq.getUsername(), token);
        }

        log.error("Authentication failed for username: {}", createUserReq.getUsername());
        throw new UnauthorizedException("Unauthorized access");
    }

    @Override
    public Set<String> getRole(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("Invalid credentials");
        }
        return user.getRoles()
                   .stream()
                   .map(Roles::getName)
                   .collect(Collectors.toSet());
    }

    private Map<String, Object> buildAuthResponse(String username, String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("roles", getRole(username));
        return response;
    }
}
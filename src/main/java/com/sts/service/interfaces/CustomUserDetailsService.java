package com.sts.service.interfaces;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sts.entity.Users;
import com.sts.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User Not Found: {}", username);
            throw new UsernameNotFoundException("User Not Found");
        }
        UserDetails userDetails = User.builder()
        		.username(user.getUsername())
        		.password(user.getPassword())
        		.roles(user.getRole())
        		.build();
        		

        log.info("User Found: {} with Role: {}", userDetails.getUsername(), userDetails.getAuthorities());
        return userDetails;
    }
}
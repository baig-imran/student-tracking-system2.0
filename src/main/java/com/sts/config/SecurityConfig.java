package com.sts.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sts.constants.Endpoints;
import com.sts.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // this annotation is used to provide role-based access control at method level.
public class SecurityConfig {
	
	private final UserDetailsService UserDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtFilter JwtFilter;
	
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtFilter jwtFilter) {
		super();
		UserDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.JwtFilter = jwtFilter;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		 provider.setPasswordEncoder(passwordEncoder);
		 provider.setUserDetailsService(UserDetailsService);
		return provider;
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
	        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(Endpoints.LOGIN, Endpoints.SIGNUP, Endpoints.LOGOUT).permitAll()
	            .anyRequest().authenticated())
	        .httpBasic(Customizer.withDefaults())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session
	        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Apply JWT filter before authentication
	    	
	    return http.build();
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow requests from this origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }
	
	


}

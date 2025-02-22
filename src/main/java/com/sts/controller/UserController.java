package com.sts.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.UserRequest;
import com.sts.service.interfaces.UserService;

@RestController
//@RequestMapping(Endpoints.V1_USER)
public class UserController {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	

	public UserController(UserService userService, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserRequest userRequest){

		return new ResponseEntity<>(userService.saveUser(userRequest), HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
	    String token = userService.verify(userRequest);
	    if (token != null) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", token);
	        response.put("userName", userRequest.getUserName());
	        
	        response.put("role", userService.getRole(userRequest.getUserName()));
	        
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }
	}
	
//	@PostConstruct
//	public void init() {
//	    UserRequest userRequest = new UserRequest();
//	    userRequest.setUserName("admin");
//	    userRequest.setPassword("1234");
//	    userRequest.setRoles(Arrays.asList(new String[]{"ADMIN", "USER"})
//	    		);
//
//
//	    userService.saveUser(userRequest);
//	}

	
	
	
	

}

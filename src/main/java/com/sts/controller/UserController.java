package com.sts.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.UserRequest;
import com.sts.dto.UserResponse;
import com.sts.service.interfaces.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.verify(userRequest));
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

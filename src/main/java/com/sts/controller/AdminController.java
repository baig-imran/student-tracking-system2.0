package com.sts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.UserRequest;
import com.sts.service.interfaces.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping("/get")
	public String get() {
		return "hello";
	}
}

package com.sts.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRequest {
	private String username;
    private String password;
    private String role;
	

}

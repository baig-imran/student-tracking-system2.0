package com.sts.dto.user;

import java.util.Set;

import lombok.Data;

@Data
public class CreateUserRes {
	private String username;
	
    private String password;
    private Set<String> roles; // or List<String>	

}

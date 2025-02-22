package com.sts.service.interfaces;

import java.util.Map;

import com.sts.dto.UserRequest;
import com.sts.dto.UserResponse;
import com.sts.entity.Users;

public interface UserService {
	
	public UserResponse saveUser(UserRequest userRequest);

	public Map<String, Object> verify(UserRequest userRequest);
	public String getRole(String username);
	

}

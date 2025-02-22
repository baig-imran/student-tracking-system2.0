package com.sts.service.interfaces;

import com.sts.dto.UserRequest;
import com.sts.dto.UserResponse;
import com.sts.entity.Users;

public interface UserService {
	
	public UserResponse saveUser(UserRequest userRequest);

	public String verify(UserRequest userRequest);
	public String getRole(String username);
	

}

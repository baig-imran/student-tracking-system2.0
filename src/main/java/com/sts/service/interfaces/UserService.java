package com.sts.service.interfaces;

import java.util.Map;
import java.util.Set;

import com.sts.dto.user.CreateUserReq;
import com.sts.dto.user.CreateUserRes;
import com.sts.entity.Users;

public interface UserService {
	
	public CreateUserRes createUser(CreateUserReq createUserReq);

	public Map<String, Object> verify(CreateUserReq createUserReq);
	public Set<String> getRole(String username);
	

}

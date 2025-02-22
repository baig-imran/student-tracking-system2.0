//package com.sts.service.interfaces;
//
//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.sts.entity.Users;
//
//
//public class UserDetailsImpl implements UserDetails{
//	
//	private Users user;
//	public UserDetailsImpl(Users user) {
//		this.user = user;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Collections.singleton(new SimpleGrantedAuthority("USER"));
//	}
//
//	@Override
//	public String getPassword() {
//		return user.getPassword();
//	}
//
//	@Override
//	public String getUsername() {
//		return user.getUserName();
//	}
//	
//
//}

package com.sts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	
	Users findByUserName(String userName);
	

}

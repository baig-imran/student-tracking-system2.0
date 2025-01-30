package com.sts.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long>{
	

}

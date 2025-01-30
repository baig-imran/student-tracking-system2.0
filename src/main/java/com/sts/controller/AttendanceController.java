package com.sts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.service.interfaces.AttendanceService;

@RestController
@RequestMapping(Endpoints.V1_ATTENDANCES)
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	public AttendanceController(AttendanceService attendanceService) {
		super();
		this.attendanceService = attendanceService;
	}

	@PostMapping
	public ResponseEntity<AttendanceResponse> createExam(@RequestBody AttendanceRequest attendanceRequest){
		
		AttendanceResponse attendanceResponse = attendanceService.saveAttedance(attendanceRequest);
		
		return new ResponseEntity<>(attendanceResponse,HttpStatus.CREATED);
		
	}


}

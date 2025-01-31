package com.sts.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.dto.AttendanceUpdateRequest;
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
	public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceRequest attendanceRequest){

		AttendanceResponse attendanceResponse = attendanceService.saveAttendance(attendanceRequest);

		return new ResponseEntity<>(attendanceResponse,HttpStatus.CREATED);

	}

	@PutMapping
	public ResponseEntity<String> updateAtendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {

		String response = attendanceService.updateAttendance(attendanceUpdateRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/bulk")
	public ResponseEntity<String> createMultipleAttendance(@RequestBody List<AttendanceRequest> attendanceRequests){

		 

		return new ResponseEntity<>(attendanceService.saveMultipleAttendance(attendanceRequests),HttpStatus.CREATED);

	}


}

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_ATTENDANCES)
public class AttendanceController {

	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		super();
		this.attendanceService = attendanceService;
	}
	
	
	@PostMapping("/search") // To get attendance using different combination of fields
	public ResponseEntity<List<AttendanceResponse>> getAttendance(@RequestBody AttendanceRequest filterRequest) {
	    log.info("Searching for attendance records with filter criteria: {}", filterRequest);
	    List<AttendanceResponse> attendanceList = attendanceService.getAttendance(filterRequest);
	    log.info("Found {} attendance records based on provided criteria", attendanceList.size());
	    return new ResponseEntity<>(attendanceList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceRequest attendanceRequest) {
	    log.info("Received request to create attendance record: {}", attendanceRequest);
	    AttendanceResponse savedAttendance = attendanceService.saveAttendance(attendanceRequest);
	    log.info("Successfully created attendance record: {}", savedAttendance);
	    return new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<String> updateAttendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {
	    log.info("Received request to update attendance record: {}", attendanceUpdateRequest);
	    String updateStatus = attendanceService.updateAttendance(attendanceUpdateRequest);
	    log.info("Attendance record update status: {}", updateStatus);
	    return new ResponseEntity<>(updateStatus, HttpStatus.OK);
	}

	@PostMapping("/bulk")
	public ResponseEntity<String> createMultipleAttendance(@RequestBody List<AttendanceRequest> attendanceRequests) {
	    log.info("Received request to create multiple attendance records: {}", attendanceRequests.size());
	    String creationStatus = attendanceService.saveMultipleAttendance(attendanceRequests);
	    log.info("Bulk attendance creation status: {}", creationStatus);
	    return new ResponseEntity<>(creationStatus, HttpStatus.CREATED);
	}

	@PutMapping("/bulk")
	public ResponseEntity<String> updateMultipleAttendance(@RequestBody List<AttendanceUpdateRequest> attendanceUpdateRequests) {
	    log.info("Received request to update multiple attendance records: {}", attendanceUpdateRequests.size());
	    String updateStatus = attendanceService.updateMultipleAttendance(attendanceUpdateRequests);
	    log.info("Bulk attendance update status: {}", updateStatus);
	    return new ResponseEntity<>(updateStatus, HttpStatus.OK);
	}


	
}

package com.sts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.SemesterRequest;
import com.sts.dto.SemesterResponse;
import com.sts.repository.SemesterRepository;
import com.sts.service.interfaces.SemesterService;

@RestController
@RequestMapping(Endpoints.V1_SEMESTERS)
public class SemesterController {
	
	private final SemesterService semesterService;
	
	
	public SemesterController(SemesterService semesterService) {
		super();
		this.semesterService = semesterService;
	}


	@PostMapping
	public ResponseEntity<SemesterResponse> createSemester(@RequestBody SemesterRequest semesterRequest){
		
		SemesterResponse semesterResponse = semesterService.saveSemester(semesterRequest);
		return new ResponseEntity<>(semesterResponse,HttpStatus.CREATED);
		
	}

}

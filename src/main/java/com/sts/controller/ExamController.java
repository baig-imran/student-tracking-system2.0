package com.sts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.service.interfaces.ExamService;

@RestController
@RequestMapping(Endpoints.V1_EXAMS)
public class ExamController {
	
	private final ExamService examService;
	
	public ExamController(ExamService examService) {
		this.examService = examService;
	}
	
	@PostMapping
	public ResponseEntity<ExamResponse> createExam(@RequestBody ExamRequest examRequest){
		
		ExamResponse examResponse = examService.saveExam(examRequest);
		
		return new ResponseEntity<>(examResponse,HttpStatus.CREATED);
		
	}


}

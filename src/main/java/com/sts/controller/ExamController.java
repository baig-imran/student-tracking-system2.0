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
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;
import com.sts.service.interfaces.ExamService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_EXAMS)
public class ExamController {
	
	private final ExamService examService;
	
	public ExamController(ExamService examService) {
		this.examService = examService;
	}
	
	
	@PostMapping("/search")
	public ResponseEntity<List<ExamResponse>> getExams(@RequestBody ExamRequest filterRequest) {
	    log.info("Searching for exams with filter criteria: {}", filterRequest);
	    List<ExamResponse> examList = examService.getExams(filterRequest);
	    log.info("Found {} exams based on the provided filter", examList.size());
	    return new ResponseEntity<>(examList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ExamResponse> createExam(@RequestBody ExamRequest examRequest) {
	    log.info("Received request to create exam: {}", examRequest);
	    ExamResponse createdExam = examService.saveExam(examRequest);
	    log.info("Successfully created exam: {}", createdExam);
	    return new ResponseEntity<>(createdExam, HttpStatus.CREATED);
	}

	@PostMapping("/bulk")
	public ResponseEntity<String> createMultipleExam(@RequestBody List<ExamRequest> examRequests) {
	    log.info("Received request to create {} exams", examRequests.size());
	    String creationStatus = examService.saveMultipleExams(examRequests);
	    log.info("Bulk exam creation status: {}", creationStatus);
	    return new ResponseEntity<>(creationStatus, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<String> updateExam(@RequestBody ExamUpdateRequest examUpdateRequest) {
	    log.info("Received request to update exam: {}", examUpdateRequest);
	    String updateStatus = examService.updateExam(examUpdateRequest);
	    log.info("Exam update status: {}", updateStatus);
	    return new ResponseEntity<>(updateStatus, HttpStatus.OK);
	}

	@PutMapping("/bulk")
	public ResponseEntity<String> updateMultipleExams(@RequestBody List<ExamUpdateRequest> examUpdateRequests) {
	    log.info("Received request to update {} exams", examUpdateRequests.size());
	    String updateStatus = examService.updateMultipleExams(examUpdateRequests);
	    log.info("Bulk exam update status: {}", updateStatus);
	    return new ResponseEntity<>(updateStatus, HttpStatus.OK);
	}

	
	


}

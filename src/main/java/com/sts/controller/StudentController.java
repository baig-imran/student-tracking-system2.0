package com.sts.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.StudentRequest;
import com.sts.dto.StudentResponse;
import com.sts.service.interfaces.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_STUDENTS)
public class StudentController {
	
	
	private final StudentService studentService;
	private final ModelMapper modelMapper;
	
	public StudentController(StudentService studentService, ModelMapper modelMapper) {
		this.studentService = studentService;
		this.modelMapper = modelMapper;
	}

	@PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest studentRequest) {
        log.info("Received request to add student: {}", studentRequest);

        StudentResponse response = studentService.saveStudent(studentRequest);
        log.info("Student added successfully with ID: {}", response.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

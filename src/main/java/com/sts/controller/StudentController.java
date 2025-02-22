package com.sts.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.dto.StudentCreateRequest;
import com.sts.dto.StudentGetRequest;
import com.sts.dto.StudentResponse;
import com.sts.entity.Student;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_STUDENTS)
public class StudentController {
	
	
	private final StudentService studentService;
	private final ModelMapper modelMapper;
	private final StudentRepository studentRepository;
	
	public StudentController(StudentService studentService, ModelMapper modelMapper, StudentRepository studentRepository) {
		this.studentService = studentService;
		this.modelMapper = modelMapper;
		this.studentRepository = studentRepository;
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<StudentResponse>> getStudents(@RequestBody StudentGetRequest studentRequest) {
	    log.info("Searching for students with criteria: {}", studentRequest);
	    return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents(studentRequest));
	}
	
	@GetMapping("/bulk")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<StudentResponse>> getStudentss() {
		List<StudentResponse> responseList = new ArrayList<>();
	   
		List<Student> students = studentRepository.findAll();
		students.forEach(student ->{
			StudentResponse response = modelMapper.map(student, StudentResponse.class);
			response.setDepartmentId(student.getDepartment().getDepartmentId());
			response.setMentorId(student.getFaculty().getFacultyId());
			responseList.add(response);
			
		});
	    return ResponseEntity.status(HttpStatus.OK).body(responseList);

	}

	@PostMapping
	public ResponseEntity<?> createStudent(@RequestBody StudentCreateRequest studentRequest) {
	    log.info("Received request to create a new student: {}", studentRequest);
	    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(studentRequest));
	}

	@PostMapping("/bulk")
	public ResponseEntity<?> createMultipleStudent(@RequestBody List<StudentCreateRequest> studentRequests) {
	    log.info("Received request to create multiple students: {}", studentRequests);
	    return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveMultipleStudents(studentRequests));
	}

}

package com.sts.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.dto.student.DeleteStudentsByIdsReq;
import com.sts.dto.student.GetStudentsByIdsReq;
import com.sts.dto.student.StudentCreateRequest;
import com.sts.dto.student.StudentGetRequest;
import com.sts.dto.student.StudentResponse;
import com.sts.dto.student.StudentUpdateRequest;
import com.sts.entity.Student;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.StudentService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(Endpoints.V1_STUDENTS)
public class StudentController {

    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable("studentId") String studentId) {
        log.info("Searching for student ID: {}", studentId);
        StudentResponse response = studentService.getStudentById(studentId);
        return ResponseBuilder.ok(response, SuccessMessageEnum.STUDENT_FETCHED, response.getStudentId());
    }

    @PostMapping("/search")
    public ResponseEntity<?> getStudentsByCriteria(@RequestBody StudentGetRequest studentRequest) {
        log.info("Searching for students with criteria: {}", studentRequest);
        List<StudentResponse> responseList = studentService.getStudentsByCriteria(studentRequest);
        return ResponseBuilder.ok(responseList, SuccessMessageEnum.STUDENTS_FETCHED, responseList.size());
    }

    @GetMapping("/bulk")
    public ResponseEntity<?> getAllStudents() {
        List<StudentResponse> responseList = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            StudentResponse response = modelMapper.map(student, StudentResponse.class);
            response.setDepartmentId(student.getDepartment().getDepartmentId());
            response.setMentorId(student.getFaculty().getFacultyId());
            responseList.add(response);
        });
        return ResponseBuilder.ok(responseList, SuccessMessageEnum.STUDENTS_FETCHED);
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentCreateRequest studentRequest) {
        log.info("Received request to create a new student: {}", studentRequest);
        StudentResponse response = studentService.saveStudent(studentRequest);
        return ResponseBuilder.created(response, SuccessMessageEnum.STUDENT_CREATED, response.getStudentId());
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createMultipleStudents(@RequestBody List<StudentCreateRequest> studentRequests) {
        log.info("Received request to create multiple students: {}", studentRequests);
        String responses = studentService.saveMultipleStudents(studentRequests);
        return ResponseBuilder.ok(null, SuccessMessageEnum.STUDENTS_BULK_CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateStudent(@RequestBody StudentUpdateRequest studentRequest) {
        log.info("Received request to update a student: {}", studentRequest);
        StudentResponse response = studentService.updateStudent(studentRequest);
        return ResponseBuilder.ok(response, SuccessMessageEnum.STUDENT_UPDATED, response.getStudentId());
    }


    @PostMapping("/getByIds")
    public ResponseEntity<?> getStudentsByIds(@RequestBody GetStudentsByIdsReq req) {
        log.info("Fetching students by IDs: {}", req.getStudentIds());
        List<StudentGetRequest> students = studentService.getStudentsByStudentIds(req);
        return ResponseBuilder.ok(students, SuccessMessageEnum.STUDENTS_FETCHED);
    }

    @PutMapping("/bulk")
    public ResponseEntity<?> updateBulkStudents(@RequestBody List<StudentUpdateRequest> reqs) {
        log.info("Received bulk update request for {} students", reqs.size());
        String result = studentService.updateBulkStudents(reqs);
        return ResponseBuilder.ok(result, SuccessMessageEnum.STUDENTS_BULK_UPDATED);
    }
    
    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") String studentId) {
    	log.info("Deleting student with ID: {}", studentId);
    	String deletedStudentId = studentService.deleteById(studentId);
    	return ResponseBuilder.ok(deletedStudentId, SuccessMessageEnum.STUDENT_DELETED, deletedStudentId);
    }
    
    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteBulkStudents(@RequestBody List<String> studentIds) {
        log.info("Deleting students with IDs: {}", studentIds);
        List<String> deletedStudentIds = studentService.deleteByIds(studentIds);
        return ResponseBuilder.ok(deletedStudentIds, SuccessMessageEnum.STUDENT_DELETED, deletedStudentIds);
    }

}
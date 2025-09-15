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
import com.sts.constants.EntityNames;
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;
import com.sts.dto.student.GetStudentsByIdsReq;
import com.sts.dto.student.CreateStudentReq;
import com.sts.dto.student.GetStudentReq;
import com.sts.dto.student.StudentResponse;
import com.sts.dto.student.UpdateStudentReq;
import com.sts.entity.Student;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.StudentService;
import com.sts.utils.ResponseBuilder1;

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
    public ResponseEntity<SuccessResponse<StudentResponse>> getStudentById(@PathVariable("studentId") String studentId) {
        log.info("Searching for student ID: {}", studentId);
        StudentResponse response = studentService.getStudentById(studentId);
        return ResponseBuilder1.ok(
                SuccessMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.STUDENT, studentId),
                response
        );    }

    @PostMapping("/search")
    public ResponseEntity<SuccessResponse<List<StudentResponse>>> getStudentsBySpecification(@RequestBody GetStudentReq req) {
        log.info("Searching for students with criteria: {}", req);
        List<StudentResponse> responseList = studentService.getStudentsBySpecification(req);
        return ResponseBuilder1.ok(
                SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(EntityNames.STUDENT, responseList.size()),
                responseList
        );
    }

    @GetMapping("/bulk")
    public ResponseEntity<SuccessResponse<List<StudentResponse>>> getAllStudents() {
        List<StudentResponse> responseList = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            StudentResponse response = modelMapper.map(student, StudentResponse.class);
            response.setDepartmentId(student.getDepartment().getDepartmentId());
            response.setMentorId(student.getFaculty().getFacultyId());
            responseList.add(response);
        });
        return ResponseBuilder1.ok(
                SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(EntityNames.STUDENT, responseList.size()),
                responseList
        );
    }
    @PostMapping
    public ResponseEntity<SuccessResponse<StudentResponse>> createStudent(@RequestBody CreateStudentReq req) {
        log.info("Received request to create a new student: {}", req);
        StudentResponse response = studentService.saveStudent(req);
        return ResponseBuilder1.created(
                SuccessMessages.ENTITY_CREATED.getMessage(EntityNames.STUDENT, response.getStudentId()),
                response
        );
    }
    @PostMapping("/bulk")
    public ResponseEntity<SuccessResponse<Object>> createBulkStudents(@RequestBody List<CreateStudentReq> req) {
        log.info("Received request to create multiple students: {}", req);
        String responses = studentService.createBulkStudents(req);
        return ResponseBuilder1.ok(
                SuccessMessages.BULK_ENTITIES_CREATED.getMessage(EntityNames.STUDENT, req.size()),
                null
        );
    }
    
    @PutMapping
    public ResponseEntity<SuccessResponse<StudentResponse>> updateStudent(@RequestBody UpdateStudentReq req) {
        log.info("Received request to update a student: {}", req);
        StudentResponse response = studentService.updateStudent(req);
        return ResponseBuilder1.ok(
                SuccessMessages.ENTITY_UPDATED.getMessage(EntityNames.STUDENT, response.getStudentId()),
                response
        );
    }

    @PostMapping("/getByIds")
    public ResponseEntity<SuccessResponse<List<GetStudentReq>>> getStudentsByIds(@RequestBody GetStudentsByIdsReq req) {
        log.info("Fetching students by IDs: {}", req.getStudentIds());
        List<GetStudentReq> students = studentService.getStudentsByStudentIds(req);
        return ResponseBuilder1.ok(
                SuccessMessages.FETCH_ALL_ENTITIES.getMessage(EntityNames.STUDENT, students.size()),
                students
        );
    }

    @PutMapping("/bulk")
    public ResponseEntity<SuccessResponse<String>> updateBulkStudents(@RequestBody List<UpdateStudentReq> reqs) {
        log.info("Received bulk update request for {} students", reqs.size());
        String result = studentService.updateBulkStudents(reqs);
        return ResponseBuilder1.ok(
                SuccessMessages.UPDATE_BULK_ENTITIES.getMessage(EntityNames.STUDENT, reqs.size()),
                result
        );
    }
    
    @DeleteMapping("/{studentId}")
    public ResponseEntity<SuccessResponse<String>> deleteStudent(@PathVariable("studentId") String studentId) {
    	log.info("Deleting student with ID: {}", studentId);
    	String deletedStudentId = studentService.deleteById(studentId);
    	 return ResponseBuilder1.ok(
                 SuccessMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.STUDENT, deletedStudentId),
                 deletedStudentId
         );
     }
    @DeleteMapping("/bulk")
    public ResponseEntity<SuccessResponse<List<String>>> deleteBulkStudents(@RequestBody List<String> studentIds) {
        log.info("Deleting students with IDs: {}", studentIds);
        List<String> deletedStudentIds = studentService.deleteByIds(studentIds);
        return ResponseBuilder1.ok(
                SuccessMessages.BULK_ENTITIES_DELETED.getMessage(EntityNames.STUDENT, deletedStudentIds.size()),
                deletedStudentIds
        );
    }
}
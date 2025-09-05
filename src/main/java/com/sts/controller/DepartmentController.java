package com.sts.controller;

import java.util.List;

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
import com.sts.dto.department.DepartmentCreateRequest;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.DepartmentUpdateRequest;
import com.sts.service.interfaces.DepartmentService;
import com.sts.utils.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoints.V1_DEPARTMENTS)
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable("departmentId") String departmentId) {
        log.info("Fetching department with ID: {}", departmentId);
        DepartmentResponse response = departmentService.getDepartmentById(departmentId);
        log.info("Successfully fetched department with ID: {}", departmentId);
        return ResponseBuilder.ok(response, SuccessMessageEnum.DEPARTMENT_FETCHED, response.getDepartmentId());
    }

    @GetMapping("/bulk")
    public ResponseEntity<?> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentResponse> responses = departmentService.getAllDepartments();
        log.info("Successfully fetched {} departments", responses.size());
        return ResponseBuilder.ok(responses, SuccessMessageEnum.DEPARTMENT_FETCHED, responses.size());
    }

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentCreateRequest request) {
        log.info("Received request to create new department: {}", request);
        DepartmentResponse response = departmentService.saveDepartment(request);
        log.info("Department created successfully with ID: {}", response.getDepartmentId());
        return ResponseBuilder.created(response, SuccessMessageEnum.DEPARTMENT_CREATED, response.getDepartmentId());
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreateDepartments(@RequestBody @Valid List<DepartmentCreateRequest> requests) {
        log.info("Received request to create {} departments", requests.size());
        String result = departmentService.bulkCreateDepartments(requests);
        log.info("Bulk department creation completed: {}", result);
        return ResponseBuilder.ok(null, SuccessMessageEnum.DEPARTMENTS_BULK_CREATED, requests.size());
    }

    @PutMapping
    public ResponseEntity<?> updateDepartment(@RequestBody @Valid DepartmentUpdateRequest request) {
        log.info("Received request to update department: {}", request);
        DepartmentResponse response = departmentService.updateDepartment(request);
        log.info("Department updated successfully with ID: {}", response.getDepartmentId());
        return ResponseBuilder.ok(response, SuccessMessageEnum.DEPARTMENT_UPDATED, response.getDepartmentId());
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable("departmentId") String departmentId) {
        log.info("Received request to delete department with ID: {}", departmentId);
        String result = departmentService.deleteDepartmentById(departmentId);
        log.info("Department deleted successfully with ID: {}", departmentId);
        return ResponseBuilder.ok(result, SuccessMessageEnum.DEPARTMENT_DELETED, departmentId);
    }
}

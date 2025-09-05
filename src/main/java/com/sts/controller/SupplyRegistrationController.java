package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.dto.exam.supplyregistration.RegisterSupplyStudentsReq;
import com.sts.service.interfaces.SupplyRegistrationService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoints.V1_SUPPLY_REGISTRATION)
public class SupplyRegistrationController {

    private final SupplyRegistrationService supplyRegistrationService;

    // GET all student IDs registered for a supply exam
    @GetMapping("/getSupplyRegisteredStudentIdsByExamCode/{examCode}")
    public ResponseEntity<?> getSupplyRegisteredStudentIdsByExamCode(@PathVariable("examCode") String examCode) {
        log.info("Fetching supply registered student IDs for examCode: {}", examCode);
        List<String> studentIds = supplyRegistrationService.getSupplyRegisteredStudentIdsByExamCode(examCode);
        return ResponseBuilder.ok(studentIds, SuccessMessageEnum.SUPPLY_REGISTERED_STUDENTS_FETCHED);
    }

    // POST register a list of students to a supply exam
    @PostMapping("/registerSupplyStudents")
    public ResponseEntity<?> registerSupplyStudents(@RequestBody RegisterSupplyStudentsReq req) {
        log.info("Received request to register supply students: {}", req);
        String serviceResponse = supplyRegistrationService.registerSupplyStudents(req);
        return ResponseBuilder.ok(serviceResponse, SuccessMessageEnum.SUPPLY_STUDENTS_REGISTERED);
    }

    // POST register multiple lists of students (bulk) to supply exams
    @PostMapping("/registerSupplyStudentsBulk")
    public ResponseEntity<?> registerSupplyStudentsBulk(@RequestBody List<RegisterSupplyStudentsReq> reqList) {
        log.info("Received bulk request to register supply students, count: {}", reqList.size());
        String result = supplyRegistrationService.registerBulkSupplyStudents(reqList);
        return ResponseBuilder.ok(result, SuccessMessageEnum.SUPPLY_STUDENTS_BULK_REGISTERED);
    }
}
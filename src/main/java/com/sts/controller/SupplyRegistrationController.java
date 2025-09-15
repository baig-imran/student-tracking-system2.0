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
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;
import com.sts.dto.exam.supplyregistration.RegisterSupplyStudentsReq;
import com.sts.service.interfaces.SupplyRegistrationService;
import com.sts.utils.ResponseBuilder;
import com.sts.utils.ResponseBuilder1;

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
    public ResponseEntity<SuccessResponse<List<String>>> getSupplyRegisteredStudentIdsByExamCode(@PathVariable("examCode") String examCode) {
        log.info("Fetching supply registered student IDs for examCode: {}", examCode);
        List<String> studentIds = supplyRegistrationService.getSupplyRegisteredStudentIdsByExamCode(examCode);
        return ResponseBuilder1.ok(
                SuccessMessages.SUPPLY_REGISTERED_STUDENTS_FETCHED.getMessage(examCode),
                studentIds
        );
    }

    // POST register a list of students to a supply exam
    @PostMapping("/registerSupplyStudents")
    public ResponseEntity<SuccessResponse<String>> registerStudentsForSupplyExam(@RequestBody RegisterSupplyStudentsReq req) {
        log.info("Received request to register supply students: {}", req);
        String serviceResponse = supplyRegistrationService.registerStudentsForSupplyExam(req);
        return ResponseBuilder1.ok(
                SuccessMessages.SUPPLY_STUDENTS_REGISTERED.getMessage(req.getExamCode()),
                serviceResponse
        );
    }

    // POST register multiple lists of students (bulk) to supply exams
    @PostMapping("/registerSupplyStudentsBulk")
    public ResponseEntity<SuccessResponse<String>> registerStudentsForSupplyExamInBulk(@RequestBody List<RegisterSupplyStudentsReq> reqList) {
        log.info("Received bulk request to register supply students, count: {}", reqList.size());
        String result = supplyRegistrationService.registerStudentsForSupplyExamInBulk(reqList);
        return ResponseBuilder1.ok(
                SuccessMessages.SUPPLY_STUDENTS_BULK_REGISTERED.getMessage(reqList.size()),
                result
        );
    }
}
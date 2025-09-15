package com.sts.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.sts.dto.exam.AddExamRequest;
import com.sts.dto.exam.AddExamResponse;
import com.sts.dto.exam.ExamsBySpecificationReq;
import com.sts.dto.exam.GetExamsBySpecificationRes;
import com.sts.dto.exam.GetExamsBySubjectCodeRes;
import com.sts.dto.exam.GetExternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetInternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetSupplyExamsBySubjectCodeRes;
import com.sts.entity.Exam;
import com.sts.service.interfaces.ExamService;
import com.sts.utils.ResponseBuilder1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_EXAMS)
public class ExamController {

    private final ExamService examService;

    @PostMapping("/search")
    public ResponseEntity<SuccessResponse<List<GetExamsBySpecificationRes>>> getExamsBySpecification(@RequestBody ExamsBySpecificationReq req) {
        log.info("Searching exams by specification: {}", req);
        List<Exam> exams = examService.getExamsBySpecification(req);

        List<GetExamsBySpecificationRes> responses = exams.stream()
                .map(examService::mapExamToGetExamBySpecificationRes)
                .collect(Collectors.toList());

        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage(responses.size()), responses);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<AddExamResponse>> createExam(@RequestBody AddExamRequest req) {
        log.info("Received request to create exam: {}", req);
        AddExamResponse response = examService.createExam(req);
        log.info("Successfully created exam: {}", response);
        return ResponseBuilder1.created(SuccessMessages.EXAM_CREATED.getMessage(response.getExamCode()), response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<SuccessResponse<String>> createBulkExams(@RequestBody List<AddExamRequest> reqs) {
        log.info("Received request to create {} exams", reqs.size());
        String result = examService.createBulkExams(reqs);
        log.info("Bulk exam creation resu	lt: {}", result);
        return ResponseBuilder1.created(SuccessMessages.EXAMS_BULK_CREATED.getMessage(reqs.size()), result);
    }

    @GetMapping("/subjectExams/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<GetExamsBySubjectCodeRes>>> getExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching exams for subjectCode: {}", subjectCode);
        List<GetExamsBySubjectCodeRes> response = examService.getExamsBySubjectCode(subjectCode);
        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage( response.size()), response);
    }

    @GetMapping("/getInternalExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<GetInternalExamsBySubjectCodeRes>>> getInternalExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching internal exams for subjectCode: {}", subjectCode);
        List<GetInternalExamsBySubjectCodeRes> response = examService.getInternalExamsBySubjectCode(subjectCode);
        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage(response.size()), response);
    }	
	
    @GetMapping("/getExternalExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<GetExternalExamsBySubjectCodeRes>>> getExternalExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching external exams for subjectCode: {}", subjectCode);
        List<GetExternalExamsBySubjectCodeRes> response = examService.getExternalExamsBySubjectCode(subjectCode);
        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage(response.size()), response);
    }

    @GetMapping("/getSupplyExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<GetSupplyExamsBySubjectCodeRes>>> getSupplyExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching supply exams for subjectCode: {}", subjectCode);
        List<GetSupplyExamsBySubjectCodeRes> response = examService.getSupplyExamsBySubjectCode(subjectCode);
        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage(response.size()), response);
    }

    @GetMapping("/getExamsBySubjectCodeAndExamTypeAndExamSubType/{subjectCode}/{examType}/{examSubType}")
    public ResponseEntity<SuccessResponse<List<GetExamsBySubjectCodeRes>>> getExamsBySubjectCodeAndExamTypeAndExamSubType(
            @PathVariable("subjectCode") String subjectCode,
            @PathVariable("examType") String examType,
            @PathVariable("examSubType") String examSubType) {
        log.info("Fetching exams for subjectCode={}, examType={}, examSubType={}", subjectCode, examType, examSubType);
        List<GetExamsBySubjectCodeRes> response =
                examService.getExamsBySubjectCodeAndExamTypeAndExamSubType(subjectCode, examType, examSubType);
        return ResponseBuilder1.ok(SuccessMessages.EXAMS_FETCHED.getMessage(response.size()), response);
    }

    // Uncomment and refactor these if needed later
    /*
    @PutMapping
    public void updateExam(@RequestBody ExamUpdateRequest req) {
        log.info("Updating exam: {}", req);
        String result = examService.updateExam(req);
        return ResponseBuilder1.ok(result, SuccessMessages.EXAM_UPDATED, req.getExamId());
    }

    @PutMapping("/bulk")
    public void updateMultipleExams(@RequestBody List<ExamUpdateRequest> reqs) {
        log.info("Updating {} exams", reqs.size());
        String result = examService.updateMultipleExams(reqs);
        return ResponseBuilder1.ok(result, SuccessMessages.EXAMS_BULK_UPDATED, reqs.size());
    }
    */
}
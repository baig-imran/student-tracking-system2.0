package com.sts.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.dto.exam.AddExamRequest;
import com.sts.dto.exam.AddExamResponse;
import com.sts.dto.exam.ExamsBySpecificationReq;
import com.sts.dto.exam.GetExamsBySpecificationRes;
import com.sts.dto.exam.GetExamsBySubjectCodeRes;
import com.sts.dto.exam.GetExternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetInternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetSupplyExamsBySubjectCodeRes;
import com.sts.entity.Exam;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.ExamService;
import com.sts.service.interfaces.StudentService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_EXAMS)
public class ExamController {

    private final ExamService examService;

    @PostMapping("/search")
    public ResponseEntity<?> getExamsBySpecification(@RequestBody ExamsBySpecificationReq req) {
        log.info("Searching exams by specification: {}", req);
        List<Exam> exams = examService.GetExamsBySpecification(req);

        List<GetExamsBySpecificationRes> responses = exams.stream()
                .map(examService::mapExamToGetExamBySpecificationRes)
                .collect(Collectors.toList());

        return ResponseBuilder.ok(responses, SuccessMessageEnum.EXAMS_FETCHED, responses.size());
    }

    @PostMapping
    public ResponseEntity<?> createExam(@RequestBody AddExamRequest req) {
        log.info("Received request to create exam: {}", req);
        AddExamResponse response = examService.createExam(req);
        log.info("Successfully created exam: {}", response);
        return ResponseBuilder.created(response, SuccessMessageEnum.EXAM_CREATED, response.getExamCode());
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreateExams(@RequestBody List<AddExamRequest> reqs) {
        log.info("Received request to create {} exams", reqs.size());
        String result = examService.bulkCreateExams(reqs);
        log.info("Bulk exam creation resu	lt: {}", result);
        return ResponseBuilder.created(result, SuccessMessageEnum.EXAMS_BULK_CREATED, reqs.size());
    }

    @GetMapping("/subjectExams/{subjectCode}")
    public ResponseEntity<?> getExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching exams for subjectCode: {}", subjectCode);
        List<GetExamsBySubjectCodeRes> response = examService.getExamsBySubjectCode(subjectCode);
        return ResponseBuilder.ok(response, SuccessMessageEnum.EXAMS_FETCHED, response.size());
    }

    @GetMapping("/getInternalExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<?> getInternalExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching internal exams for subjectCode: {}", subjectCode);
        List<GetInternalExamsBySubjectCodeRes> response = examService.getInternalExamsBySubjectCode(subjectCode);
        return ResponseBuilder.ok(response, SuccessMessageEnum.EXAMS_FETCHED, response.size());
    }	
	
    @GetMapping("/getExternalExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<?> getExternalExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching external exams for subjectCode: {}", subjectCode);
        List<GetExternalExamsBySubjectCodeRes> response = examService.getExternalExamsBySubjectCode(subjectCode);
        return ResponseBuilder.ok(response, SuccessMessageEnum.EXAMS_FETCHED, response.size());
    }

    @GetMapping("/getSupplyExamsBySubjectCode/{subjectCode}")
    public ResponseEntity<?> getSupplyExamsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching supply exams for subjectCode: {}", subjectCode);
        List<GetSupplyExamsBySubjectCodeRes> response = examService.getSupplyExamsBySubjectCode(subjectCode);
        return ResponseBuilder.ok(response, SuccessMessageEnum.EXAMS_FETCHED, response.size());
    }

    @GetMapping("/getExamsBySubjectCodeAndExamTypeAndExamSubType/{subjectCode}/{examType}/{examSubType}")
    public ResponseEntity<?> getExamsBySubjectCodeAndExamTypeAndExamSubType(
            @PathVariable("subjectCode") String subjectCode,
            @PathVariable("examType") String examType,
            @PathVariable("examSubType") String examSubType) {
        log.info("Fetching exams for subjectCode={}, examType={}, examSubType={}", subjectCode, examType, examSubType);
        List<GetExamsBySubjectCodeRes> response =
                examService.getExamsBySubjectCodeAndExamTypeAndExamSubType(subjectCode, examType, examSubType);
        return ResponseBuilder.ok(response, SuccessMessageEnum.EXAMS_FETCHED, response.size());
    }

    // Uncomment and refactor these if needed later
    /*
    @PutMapping
    public ResponseEntity<?> updateExam(@RequestBody ExamUpdateRequest req) {
        log.info("Updating exam: {}", req);
        String result = examService.updateExam(req);
        return ResponseBuilder.ok(result, SuccessMessageEnum.EXAM_UPDATED, req.getExamId());
    }

    @PutMapping("/bulk")
    public ResponseEntity<?> updateMultipleExams(@RequestBody List<ExamUpdateRequest> reqs) {
        log.info("Updating {} exams", reqs.size());
        String result = examService.updateMultipleExams(reqs);
        return ResponseBuilder.ok(result, SuccessMessageEnum.EXAMS_BULK_UPDATED, reqs.size());
    }
    */
}
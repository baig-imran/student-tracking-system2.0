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
import com.sts.dto.subjects.SubjectCreateRequest;
import com.sts.dto.subjects.SubjectGetRequest;
import com.sts.dto.subjects.SubjectResponse;
import com.sts.dto.subjects.SubjectUpdateRequest;
import com.sts.service.interfaces.SubjectService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_SUBJECTS)
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<?> createSubject(@RequestBody SubjectCreateRequest request) {
        log.info("Received request to create subject: {}", request);
        SubjectResponse response = subjectService.saveSubject(request);
        return ResponseBuilder.created(response, SuccessMessageEnum.SUBJECT_CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBulkSubject(@RequestBody List<SubjectCreateRequest> requests) {
        log.info("Received request to create {} subjects", requests.size());
        List<SubjectResponse> responses = subjectService.saveMultipleSubjects(requests);
        return ResponseBuilder.created(responses, SuccessMessageEnum.SUBJECTS_CREATED);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getSubject(@PathVariable("subjectId") String subjectId) {
        log.info("Fetching subject with id: {}", subjectId);
        SubjectResponse response = subjectService.getSubjectById(subjectId);
        return ResponseBuilder.ok(response, SuccessMessageEnum.SUBJECT_FETCHED);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getSubjectsByCriteria(@RequestBody SubjectGetRequest req) {
        log.info("Searching for subjects with criteria: {}", req);
        List<SubjectResponse> responses = subjectService.getSubjectsByCriteria(req);
        return ResponseBuilder.ok(responses, SuccessMessageEnum.SUBJECTS_FETCHED);
    }

    @GetMapping
    public ResponseEntity<?> getAllSubjects() {
        log.info("Fetching all subjects");
        List<SubjectResponse> responses = subjectService.getAllSubjects();
        return ResponseBuilder.ok(responses, SuccessMessageEnum.SUBJECTS_FETCHED);
    }

    @PutMapping
    public ResponseEntity<?> updateSubject(@RequestBody SubjectUpdateRequest request) {
        log.info("Updating subject: {}", request);
        SubjectResponse response = subjectService.updateSubject(request);
        return ResponseBuilder.ok(response, SuccessMessageEnum.SUBJECT_UPDATED);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable("subjectId") String subjectId) {
        log.info("Deleting subject with id: {}", subjectId);
        String result = subjectService.deleteSubjectById(subjectId);
        return ResponseBuilder.ok(result, SuccessMessageEnum.SUBJECT_DELETED);
    }
}
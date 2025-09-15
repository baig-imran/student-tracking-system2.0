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

import com.sts.constants.APILogDebugMessages;
import com.sts.constants.APILogInfoMessages;
import com.sts.constants.Endpoints;
import com.sts.constants.EntityNames;
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;
import com.sts.dto.subjects.SubjectCreateRequest;
import com.sts.dto.subjects.SubjectGetRequest;
import com.sts.dto.subjects.SubjectResponse;
import com.sts.dto.subjects.SubjectUpdateRequest;
import com.sts.service.interfaces.SubjectService;
import com.sts.utils.ResponseBuilder;
import com.sts.utils.ResponseBuilder1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_SUBJECTS)
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SuccessResponse<SubjectResponse>> createSubject(@RequestBody SubjectCreateRequest req) {
        log.info(APILogInfoMessages.CREATE_ENTITY_WITH_ID.getMessage(EntityNames.SUBJECT, req.getSubjectId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.SUBJECT, req));
        SubjectResponse response = subjectService.response(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECT, response));
        log.info(APILogInfoMessages.ENTITY_CREATED_WITH_ID.getMessage(EntityNames.SUBJECT, response.getSubjectId()));
        return ResponseBuilder1.created(SuccessMessages.ENTITY_CREATED.getMessage(EntityNames.SUBJECT, response.getSubjectId()), response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<SuccessResponse<List<SubjectResponse>>> createBulkSubjects(@RequestBody List<SubjectCreateRequest> req) {
        log.info(APILogInfoMessages.CREATE_BULK_ENTITIES_WITH_SIZE.getMessage(EntityNames.SUBJECTS, req.size()));
        log.debug(APILogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.SUBJECTS, req.size(), req));
        List<SubjectResponse> responses = subjectService.createBulkSubjects(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECTS, responses));
        log.info(APILogInfoMessages.BULK_ENTITIES_CREATED_WITH_SIZE.getMessage(EntityNames.SUBJECTS, responses.size()));
        return ResponseBuilder1.created(SuccessMessages.BULK_ENTITIES_CREATED.getMessage(req.size(), EntityNames.SUBJECTS), responses);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<SuccessResponse<SubjectResponse>> getSubjectById(@PathVariable("subjectId") String subjectId) {
        log.info(APILogInfoMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId));
        SubjectResponse response = subjectService.getSubjectById(subjectId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECT, response));
        log.info(APILogInfoMessages.ENTITY_FETCHED_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId));
        return ResponseBuilder1.ok(SuccessMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId),response);
    }

    @PostMapping("/search")
    public ResponseEntity<SuccessResponse<List<SubjectResponse>>> getSubjectsBySpecification(@RequestBody SubjectGetRequest req) {
        log.info(APILogInfoMessages.FETCHING_ENTITIES_BY_SPECIFICATION.getMessage(EntityNames.SUBJECTS));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.SUBJECTS, req));
        List<SubjectResponse> responses = subjectService.getSubjectsBySpecification(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECTS, responses));
        log.info(APILogInfoMessages.ENTITIES_FETCHED_BY_SPECIFICATION.getMessage(responses.size(), EntityNames.SUBJECTS));
        return ResponseBuilder1.ok(SuccessMessages.ENTITIES_FETCHED_BY_SPECIFICATION.getMessage(responses.size(), EntityNames.SUBJECTS), responses);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<SubjectResponse>>> getAllSubjects() {
        log.info(APILogInfoMessages.FETCHING_ALL_ENTITIES.getMessage(EntityNames.SUBJECTS));
        List<SubjectResponse> responses = subjectService.getAllSubjects();
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECTS, responses));
        log.info(APILogInfoMessages.ALL_ENTITIES_FETCHED.getMessage(responses.size(), EntityNames.SUBJECTS));
        return ResponseBuilder1.ok(SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(responses.size(), EntityNames.SUBJECTS), responses);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<SubjectResponse>> updateSubject(@RequestBody SubjectUpdateRequest request) {
        log.info(APILogInfoMessages.UPDATE_ENTITY_WITH_ID.getMessage(EntityNames.SUBJECT, request.getSubjectId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.SUBJECT, request));
        SubjectResponse response = subjectService.updateSubject(request);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECT, response));
        log.info(APILogInfoMessages.ENTITY_UPDATED_WITH_ID.getMessage(EntityNames.SUBJECT, response.getSubjectId()));
        return ResponseBuilder1.ok(SuccessMessages.ENTITY_UPDATED.getMessage(EntityNames.SUBJECT, response.getSubjectId()), response);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<SuccessResponse<String>> deleteSubjectById(@PathVariable("subjectId") String subjectId) {
        log.info(APILogInfoMessages.DELETE_ENTITY_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId));
        String result = subjectService.deleteSubjectById(subjectId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.SUBJECT, result));
        log.info(APILogInfoMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId));
        return ResponseBuilder1.ok(result, SuccessMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.SUBJECT, subjectId));
    }
}
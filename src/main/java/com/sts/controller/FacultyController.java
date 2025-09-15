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
import com.sts.dto.faculty.FacultyCreateRequest;
import com.sts.dto.faculty.FacultyGetRequest;
import com.sts.dto.faculty.FacultyResponse;
import com.sts.dto.faculty.FacultyUpdateRequest;
import com.sts.service.interfaces.FacultyService;
import com.sts.utils.ResponseBuilder1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_FACULTIES)
public class FacultyController {

    private final FacultyService facultyService;


    @GetMapping("/{facultyId}")
    public ResponseEntity<SuccessResponse<FacultyResponse>> getFacultyById(@PathVariable("facultyId") String facultyId) {
        log.info(APILogInfoMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY, facultyId));
        FacultyResponse res = facultyService.getFacultyById(facultyId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTY, res));
        log.info(APILogInfoMessages.ENTITY_FETCHED_WITH_ID.getMessage(EntityNames.FACULTY, facultyId));
        return ResponseBuilder1.ok(SuccessMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY, facultyId), res);
    }

    @PostMapping("/search")
    public ResponseEntity<SuccessResponse<List<FacultyResponse>>> getFacultyBySpecification(@RequestBody FacultyGetRequest req) {
        log.info(APILogInfoMessages.SEARCH_ENTITIES.getMessage(EntityNames.FACULTIES));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTIES, req));
        List<FacultyResponse> res = facultyService.getFacultyBySpecification(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTIES, res));
        log.info(APILogInfoMessages.ENTITIES_SEARCHED.getMessage(EntityNames.FACULTIES, res.size()));
        return ResponseBuilder1.ok(SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(res.size(), EntityNames.FACULTIES), res);
    }

    @GetMapping("/bulk")
    public ResponseEntity<SuccessResponse<List<FacultyResponse>>> getAllFaculties() {
        log.info(APILogInfoMessages.FETCHING_ALL_ENTITIES.getMessage(EntityNames.FACULTIES));
        List<FacultyResponse> res = facultyService.getAllFaculties();
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTIES, res));
        log.info(APILogInfoMessages.ALL_ENTITIES_FETCHED.getMessage(res.size(), EntityNames.FACULTIES));
        return ResponseBuilder1.ok(SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(res.size(), EntityNames.FACULTIES), res);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<FacultyResponse>> createFaculty(@RequestBody FacultyCreateRequest req) {
        log.info(APILogInfoMessages.CREATE_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY, req.getFacultyId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTY, req));
        FacultyResponse res = facultyService.createFaculty(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTY, res));
        log.info(APILogInfoMessages.ENTITY_CREATED_WITH_ID.getMessage(EntityNames.FACULTY, res.getFacultyId()));
        return ResponseBuilder1.created(SuccessMessages.ENTITY_CREATED.getMessage(EntityNames.FACULTY, res.getFacultyId()), res);
    }

    @PostMapping("/bulk")
    public ResponseEntity<SuccessResponse<Object>> addBulkFaculties(@RequestBody List<FacultyCreateRequest> req) {
        log.info(APILogInfoMessages.CREATE_BULK_ENTITIES_WITH_SIZE.getMessage(EntityNames.FACULTIES, req.size()));
        log.debug(APILogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.FACULTIES, req.size(), req));
        String res = facultyService.addBulkFaculties(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTIES, res));
        log.info(APILogInfoMessages.BULK_ENTITIES_CREATED_WITH_SIZE.getMessage(EntityNames.FACULTIES, req.size()));
        return ResponseBuilder1.ok(SuccessMessages.BULK_ENTITIES_CREATED.getMessage(req.size(), EntityNames.FACULTIES), res);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<FacultyResponse>> updateFaculty(@RequestBody FacultyUpdateRequest request) {
        log.info(APILogInfoMessages.UPDATE_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY, request.getFacultyId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTY, request));
        FacultyResponse res = facultyService.updateFaculty(request);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTY, res));
        log.info(APILogInfoMessages.ENTITY_UPDATED_WITH_ID.getMessage(EntityNames.FACULTY, res.getFacultyId()));
        return ResponseBuilder1.ok(SuccessMessages.ENTITY_UPDATED.getMessage(EntityNames.FACULTY, res.getFacultyId()), res);
    }

    @DeleteMapping("/{facultyId}")
    public ResponseEntity<SuccessResponse<String>> deleteFacultyById(@PathVariable("facultyId") String facultyId) {
        log.info(APILogInfoMessages.DELETE_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY, facultyId));
        String res = facultyService.deleteFacultyById(facultyId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.FACULTY, res));
        log.info(APILogInfoMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.FACULTY, facultyId));
        return ResponseBuilder1.ok(SuccessMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.FACULTY, facultyId), res);
    }
}
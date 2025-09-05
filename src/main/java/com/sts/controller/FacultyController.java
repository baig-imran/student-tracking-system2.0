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
import com.sts.dto.faculty.FacultyCreateRequest;
import com.sts.dto.faculty.FacultyGetRequest;
import com.sts.dto.faculty.FacultyResponse;
import com.sts.dto.faculty.FacultyUpdateRequest;
import com.sts.service.interfaces.FacultyService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_FACULTIES)
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping("/{facultyId}")
    public ResponseEntity<?> getFacultyById(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching faculty with ID: {}", facultyId);
        FacultyResponse response = facultyService.getFacultyById(facultyId);
        return ResponseBuilder.ok(response, SuccessMessageEnum.FACULTY_FETCHED, facultyId);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getFaculty(@RequestBody FacultyGetRequest facultyGetRequest) {
        log.info("Searching faculties with criteria: {}", facultyGetRequest);
        List<FacultyResponse> responses = facultyService.getFacultyByCriteria(facultyGetRequest);
        return ResponseBuilder.ok(responses, SuccessMessageEnum.FACULTIES_FETCHED, responses.size());
    }

    @GetMapping
    public ResponseEntity<?> getAllFaculties() {
        log.info("Fetching all faculties");
        List<FacultyResponse> responses = facultyService.getAllFaculties();
        return ResponseBuilder.ok(responses, SuccessMessageEnum.FACULTIES_FETCHED, responses.size());
    }

    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyCreateRequest request) {
        log.info("Creating faculty: {}", request);
        FacultyResponse response = facultyService.saveFaculty(request);
        return ResponseBuilder.created(response, SuccessMessageEnum.FACULTY_CREATED, response.getFacultyId());
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createMultipleFaculties(@RequestBody List<FacultyCreateRequest> requests) {
        log.info("Creating multiple faculties: {}", requests.size());
        List<FacultyResponse> responses = facultyService.saveMultipleFaculties(requests);
        return ResponseBuilder.created(responses, SuccessMessageEnum.FACULTIES_BULK_CREATED, requests.size());
    }

    @PutMapping
    public ResponseEntity<?> updateFaculty(@RequestBody FacultyUpdateRequest request) {
        log.info("Updating faculty: {}", request);
        FacultyResponse response = facultyService.updateFaculty(request);
        return ResponseBuilder.ok(response, SuccessMessageEnum.FACULTY_UPDATED, response.getFacultyId());
    }

    @DeleteMapping("/{facultyId}")
    public ResponseEntity<?> deleteFaculty(@PathVariable("facultyId") String facultyId) {
        log.info("Deleting faculty with ID: {}", facultyId);
        String result = facultyService.deleteFacultyById(facultyId);
        return ResponseBuilder.ok(result, SuccessMessageEnum.FACULTY_DELETED, facultyId);
    }
}
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
import com.sts.dto.semestersubject.FacultySemesterSubjectStudentsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse2;
import com.sts.service.interfaces.SemesterSubjectService;
import com.sts.utils.ResponseBuilder;
import com.sts.utils.ResponseBuilder1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_SUBJECTS_OF_SEMESTER)
public class SemesterSubjectController {

    private final SemesterSubjectService semesterSubjectService;

    @GetMapping("/{facultyId}")
    public ResponseEntity<SuccessResponse<FacultySemesterSubjectsGetResponse2>> getActiveFacultySemesterSubjectsByFacultyId(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching active semester subjects for facultyId: {}", facultyId);
        FacultySemesterSubjectsGetResponse2 response =
                semesterSubjectService.getActiveSemesterSubjectsByFacultyId(facultyId);

        return ResponseBuilder1.ok(
                SuccessMessages.FACULTY_SEMESTER_SUBJECTS_FETCHED.getMessage(facultyId),
                response
        );
    }

    @PostMapping("/active-semester-subject-students")
    public ResponseEntity<SuccessResponse<List<String>>> getActiveSemesterSubjectStudents(@RequestBody FacultySemesterSubjectStudentsGetRequest req) {
        log.info("Fetching active semester subject students with request: {}", req);
        List<String> responses = semesterSubjectService.getActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(req);

        return ResponseBuilder1.ok(
                SuccessMessages.SEMESTER_SUBJECT_STUDENTS_FETCHED.getMessage(responses.size()),
                responses
        );
    }

    @PostMapping("/active-semester-subjects")
    public ResponseEntity<SuccessResponse<FacultySemesterSubjectsGetResponse>> getActiveSemesterSubjects(@RequestBody FacultySemesterSubjectsGetRequest req) {
        log.info("Fetching active semester subjects with request: {}", req);
        FacultySemesterSubjectsGetResponse response = semesterSubjectService.getActiveSemesterSubjectsByFaculty(req);

        return ResponseBuilder1.ok(
                SuccessMessages.ACTIVE_SEMESTER_SUBJECTS_FETCHED.getMessage(),
                response
        );
    }
}
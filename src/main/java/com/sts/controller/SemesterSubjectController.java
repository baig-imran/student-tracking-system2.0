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
import com.sts.dto.semestersubject.FacultySemesterSubjectStudentsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse2;
import com.sts.service.interfaces.SemesterSubjectService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_SUBJECTS_OF_SEMESTER)
public class SemesterSubjectController {

    private final SemesterSubjectService semesterSubjectService;

    @GetMapping("/{facultyId}")
    public ResponseEntity<?> getActiveFacultySemesterSubjectsByFacultyId(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching active semester subjects for facultyId: {}", facultyId);
        FacultySemesterSubjectsGetResponse2 response =
                semesterSubjectService.getActiveSemesterSubjectsByFacultyId(facultyId);

        return ResponseBuilder.ok(response,
                SuccessMessageEnum.FACULTY_SEMESTER_SUBJECTS_FETCHED, facultyId);
    }

    @PostMapping("/active-semester-subject-students")
    public ResponseEntity<?> getActiveSemesterSubjectStudents(@RequestBody FacultySemesterSubjectStudentsGetRequest req) {
        log.info("Fetching active semester subject students with request: {}", req);
        List<String> students = semesterSubjectService.getActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(req);

        return ResponseBuilder.ok(students, SuccessMessageEnum.SEMESTER_SUBJECT_STUDENTS_FETCHED);
    }

    @PostMapping("/active-semester-subjects")
    public ResponseEntity<?> getActiveSemesterSubjects(@RequestBody FacultySemesterSubjectsGetRequest req) {
        log.info("Fetching active semester subjects with request: {}", req);
        FacultySemesterSubjectsGetResponse response = semesterSubjectService.getActiveSemesterSubjectsByFaculty(req);

        return ResponseBuilder.ok(response, SuccessMessageEnum.ACTIVE_SEMESTER_SUBJECTS_FETCHED);
    }
}
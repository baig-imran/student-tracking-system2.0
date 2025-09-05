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
import com.sts.constants.SuccessResponse;
import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;
import com.sts.service.interfaces.StudentSubjectService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_STUDENT_SUBJECTS)
public class StudentSubjectController {

    private final StudentSubjectService studentSubjectService;

    @PostMapping("/addStudentsToSubject")
    public ResponseEntity<SuccessResponse<List<AddStudentsToSubjectRes>>> addStudentsToSubject(
            @RequestBody List<AddStudentsToSubjectReq> reqList) {

        log.info("Received request to map students to subjects: {}", reqList);

        List<AddStudentsToSubjectRes> resList = studentSubjectService.addStudentsToSubject(reqList);

        return ResponseBuilder.ok(resList, SuccessMessageEnum.STUDENTS_ADDED_TO_SUBJECT);
    }


    @GetMapping("/students/{subjectCode}")
    public ResponseEntity<?> getSubjectStudentsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching students for subjectCode: {}", subjectCode);
        List<String> students = studentSubjectService.getSubjectStudentsBySubjectCode(subjectCode);

        return ResponseBuilder.ok(students, SuccessMessageEnum.SUBJECT_STUDENTS_FETCHED, subjectCode);
    }
}
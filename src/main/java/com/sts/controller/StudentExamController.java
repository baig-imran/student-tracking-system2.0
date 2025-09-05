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
import com.sts.dto.exam.studentexam.AddStudentsExamDataReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterInternalExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentCompleteResultRes;
import com.sts.service.interfaces.ExamService;
import com.sts.service.interfaces.StudentExamService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_STUDENT_EXAMS)
public class StudentExamController {

    private final StudentExamService studentExamService;
    private final ExamService examService;

    @PostMapping("/addStudentsExamData")
    public ResponseEntity<?> addStudentsToExam(@RequestBody AddStudentsExamDataReq req) {
        log.info("Received request to add students to exam: {}", req);
        AddStudentsExamDataRes response = studentExamService.addStudentsExamData(req);

        return ResponseBuilder.created(response,
                SuccessMessageEnum.STUDENTS_ADDED_TO_EXAM);
    }

    @PostMapping("/addMultipleSubjectExamData")
    public ResponseEntity<?> addMultipleSubjectExamData(@RequestBody List<AddStudentsExamDataReq> req) {
        log.info("Received request to upload multiple subject exam data, count: {}", req.size());
        studentExamService.addMultipleSubjectExamDataUpload(req);

        return ResponseBuilder.created("Multiple exam data uploaded successfully",
                SuccessMessageEnum.MULTIPLE_EXAM_DATA_UPLOADED);
    }

    @GetMapping("/examSubjectStudents/{examCode}")
    public ResponseEntity<?> getExamStudentsByExamCode(@PathVariable("examCode") String examCode) {
        log.info("Fetching students for examCode: {}", examCode);
        List<String> students = studentExamService.getExamStudentsByExamCode(examCode);

        return ResponseBuilder.ok(students,
                SuccessMessageEnum.EXAM_STUDENTS_FETCHED, examCode);
    }

    @GetMapping("/getStudentAllSemesterExamDetails/{studentId}")
    public ResponseEntity<?> getStudentAllSemesterExamDetails(@PathVariable("studentId") String studentId) {
        log.info("Fetching all semester exam details for studentId: {}", studentId);
        GetStudentAllSemesterExamDetailsRes response =
                studentExamService.getStudentAllSemesterExamDetails(studentId);

        return ResponseBuilder.ok(response,
                SuccessMessageEnum.STUDENT_ALL_SEMESTER_EXAMS_FETCHED, studentId);
    }

    @GetMapping("/getStudentAllSemesterInternalExamDetails/{studentId}")
    public ResponseEntity<?> getStudentAllSemesterInternalExamDetails(@PathVariable("studentId") String studentId) {
        log.info("Fetching all semester internal exam details for studentId: {}", studentId);
        GetStudentAllSemesterInternalExamDetailsRes response =
                studentExamService.getStudentAllSemesterInternalExamDetails(studentId);

        return ResponseBuilder.ok(response,
                SuccessMessageEnum.STUDENT_ALL_SEMESTER_INTERNAL_EXAMS_FETCHED, studentId);
    }

    @GetMapping("/getInternalExamQualifiedStudentsBySubject/{subjectCode}")
    public ResponseEntity<?> getInternalExamQualifiedStudentsBySubject(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching qualified students for internal exam of subject: {}", subjectCode);
        List<String> students = studentExamService.getInternalExamQualifiedStudentsBySubject(subjectCode);

        return ResponseBuilder.ok(students,
                SuccessMessageEnum.INTERNAL_EXAM_QUALIFIED_STUDENTS_FETCHED, subjectCode);
    }

    @GetMapping("/getSEEFailedStudentsByExam/{examCode}")
    public ResponseEntity<?> getSEEFailedStudentsByExam(@PathVariable("examCode") String examCode) {
        log.info("Fetching failed students for exam: {}", examCode);
        List<String> students = studentExamService.getSEEFailedStudentsByExam(examCode);

        return ResponseBuilder.ok(students,
                SuccessMessageEnum.SEE_FAILED_STUDENTS_FETCHED, examCode);
    }

    @GetMapping("/getStudentOverallMarks/{studentId}")
    public ResponseEntity<?> getStudentOverallMarks(@PathVariable("studentId") String studentId) {
        log.info("Fetching overall marks for studentId: {}", studentId);
        GetStudentCompleteResultRes response = studentExamService.getStudentOverallMarks(studentId);

        return ResponseBuilder.ok(response,
                SuccessMessageEnum.STUDENT_OVERALL_MARKS_FETCHED, studentId);
    }
}
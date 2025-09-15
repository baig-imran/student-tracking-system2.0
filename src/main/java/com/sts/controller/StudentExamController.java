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
import com.sts.dto.exam.studentexam.AddStudentsExamDataReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataRes;
import com.sts.dto.exam.studentexam.GetInternalMarksByStudentIdAndSemesterCodeRes;
import com.sts.dto.exam.studentexam.GetLowExternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetLowInternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterInternalExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentCompleteResultRes;
import com.sts.dto.exam.studentexam.GetStudentsWithSupplyByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetSupplyExamDetailsByStudentIdRes;
import com.sts.service.interfaces.ExamService;
import com.sts.service.interfaces.StudentExamService;
import com.sts.utils.ResponseBuilder1;

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
    public ResponseEntity<SuccessResponse<AddStudentsExamDataRes>> addStudentsExamData(@RequestBody AddStudentsExamDataReq req) {
        log.info("Received request to add students to exam: {}", req);
        AddStudentsExamDataRes response = studentExamService.addStudentsExamData(req);

        return ResponseBuilder1.created(
                SuccessMessages.STUDENTS_ADDED_TO_EXAM.getMessage(),
                response
        );
    }

    @PostMapping("/addMultipleSubjectExamData")
    public ResponseEntity<SuccessResponse<String>> addBulkSubjectExamData(@RequestBody List<AddStudentsExamDataReq> req) {
        log.info("Received request to upload multiple subject exam data, count: {}", req.size());
        studentExamService.addMultipleSubjectExamDataUpload(req);

        return ResponseBuilder1.created(
                SuccessMessages.MULTIPLE_EXAM_DATA_UPLOADED.getMessage(req.size()),
                "Multiple exam data uploaded successfully"
        );
    }
    @GetMapping("/examSubjectStudents/{examCode}")
    public ResponseEntity<SuccessResponse<List<String>>> getStudentsByExamCode(@PathVariable("examCode") String examCode) {
        log.info("Fetching students for examCode: {}", examCode);
        List<String> students = studentExamService.getStudentsByExamCode(examCode);

        return ResponseBuilder1.ok(
                SuccessMessages.EXAM_STUDENTS_FETCHED.getMessage(examCode),
                students
        );
    }
    @GetMapping("/getStudentAllSemesterExamDetails/{studentId}")
    public ResponseEntity<SuccessResponse<GetStudentAllSemesterExamDetailsRes>> getAllSemestersExamDataByStudentId(@PathVariable("studentId") String studentId) {
        log.info("Fetching all semester exam details for studentId: {}", studentId);
        GetStudentAllSemesterExamDetailsRes response =
                studentExamService.getAllSemestersExamDataByStudentId(studentId);

        return ResponseBuilder1.ok(
                SuccessMessages.STUDENT_ALL_SEMESTER_EXAMS_FETCHED.getMessage(studentId),
                response
        );
    }
    @GetMapping("/getStudentAllSemesterInternalExamDetails/{studentId}")
    public ResponseEntity<SuccessResponse<GetStudentAllSemesterInternalExamDetailsRes>> getAllSemestersInternalExamDataByStudentId(@PathVariable("studentId") String studentId) {
        log.info("Fetching all semester internal exam details for studentId: {}", studentId);
        GetStudentAllSemesterInternalExamDetailsRes response =
                studentExamService.getAllSemestersInternalExamDataByStudentId(studentId);

        return ResponseBuilder1.ok(
                SuccessMessages.STUDENT_ALL_SEMESTER_INTERNAL_EXAMS_FETCHED.getMessage(studentId),
                response
        );
    }

    @GetMapping("/getInternalExamQualifiedStudentsBySubject/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<String>>> getInternalExamQualifiedStudentsBySubjectCode(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching qualified students for internal exam of subject: {}", subjectCode);
        List<String> students = studentExamService.getInternalExamQualifiedStudentsBySubjectCode(subjectCode);

        return ResponseBuilder1.ok(
                SuccessMessages.INTERNAL_EXAM_QUALIFIED_STUDENTS_FETCHED.getMessage(subjectCode),
                students
        );
    }
    
    @GetMapping("/getInternalExamDisQualifiedStudentsBySubject/{subjectCode}")
    public ResponseEntity<SuccessResponse<List<String>>> getInternalExamDisQualifiedStudentsBySubjectCode(
            @PathVariable("subjectCode") String subjectCode) {

        log.info("Fetching internal exam disqualified students for subjectCode: {}", subjectCode);

        List<String> disqualifiedStudents =
                studentExamService.getInternalExamDisQualifiedStudentsBySubjectCode(subjectCode);

        return ResponseBuilder1.ok(
                SuccessMessages.INTERNAL_DISQUALIFIED_STUDENTS_FETCHED.getMessage(subjectCode),
                disqualifiedStudents
        );
    }


    @GetMapping("/getSEEFailedStudentsByExam/{examCode}")
    public ResponseEntity<SuccessResponse<List<String>>> getSEEFailedStudentsByExamCode(@PathVariable("examCode") String examCode) {
        log.info("Fetching failed students for exam: {}", examCode);
        List<String> students = studentExamService.getSEEFailedStudentsByExamCode(examCode);

        return ResponseBuilder1.ok(
                SuccessMessages.SEE_FAILED_STUDENTS_FETCHED.getMessage(examCode),
                students
        );
    }

    @GetMapping("/getStudentOverallMarks/{studentId}")
    public ResponseEntity<SuccessResponse<GetStudentCompleteResultRes>> getOverallMarksByStudentId(@PathVariable("studentId") String studentId) {
        log.info("Fetching overall marks for studentId: {}", studentId);
        GetStudentCompleteResultRes response = studentExamService.getOverallMarksByStudentId(studentId);

        return ResponseBuilder1.ok(
                SuccessMessages.STUDENT_OVERALL_MARKS_FETCHED.getMessage(studentId),
                response
        );
    }
    
    @GetMapping("/getLowInternalMarksStudentsByFacultyId/{facultyId}")
    public ResponseEntity<SuccessResponse<List<GetLowInternalMarksStudentsByFacultyIdRes>>> getLowInternalMarksStudentsByFacultyId(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching low internal marks students for facultyId: {}", facultyId);

        List<GetLowInternalMarksStudentsByFacultyIdRes> response =
                studentExamService.getLowInternalMarksStudentsByFacultyId(facultyId);

        return ResponseBuilder1.ok(
                SuccessMessages.LOW_INTERNAL_MARKS_STUDENTS_FETCHED.getMessage(facultyId),
                response
        );
    }
    
    @GetMapping("/getInternalMarksByStudentIdAndSemesterCode/{studentId}/{semesterCode}")
    public ResponseEntity<SuccessResponse<List<GetInternalMarksByStudentIdAndSemesterCodeRes>>> getInternalMarksByStudentIdAndSemesterCode(
            @PathVariable("studentId") String studentId,
            @PathVariable("semesterCode") String semesterCode) {

        log.info("Fetching internal marks for studentId: {}, semesterCode: {}", studentId, semesterCode);

        List<GetInternalMarksByStudentIdAndSemesterCodeRes> response =
                studentExamService.getInternalMarksByStudentIdAndSemesterCode(studentId, semesterCode);

        return ResponseBuilder1.ok(
                SuccessMessages.INTERNAL_MARKS_FETCHED.getMessage(studentId, semesterCode),
                response
        );
    }
    
    @GetMapping("/getLowExternalMarksStudentsByFacultyId/{facultyId}")
    public ResponseEntity<SuccessResponse<List<GetLowExternalMarksStudentsByFacultyIdRes>>> getLowExternalMarksStudentsByFacultyId(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching students with low external marks for facultyId: {}", facultyId);
        List<GetLowExternalMarksStudentsByFacultyIdRes> response = studentExamService.getLowExternalMarksStudentsByFacultyId(facultyId);
        return ResponseBuilder1.ok(
                SuccessMessages.LOW_EXTERNAL_MARKS_FETCHED.getMessage(facultyId),
                response
        );
    }
    
    @GetMapping("/getStudentsWithSupplyByFacultyId/{facultyId}")
    public ResponseEntity<SuccessResponse<List<GetStudentsWithSupplyByFacultyIdRes>>> getStudentsWithSupplyByFacultyId(@PathVariable("facultyId") String facultyId) {
        log.info("Fetching students with supply (failed external subjects) for facultyId: {}", facultyId);
        List<GetStudentsWithSupplyByFacultyIdRes> response = studentExamService.getStudentsWithSupplyByFacultyId(facultyId);
        return ResponseBuilder1.ok(
                SuccessMessages.SUPPLY_STUDENTS_FETCHED_BY_FACULTY.getMessage(facultyId),
                response
        );
    }
    
    @GetMapping("/getSupplyExamDetailsByStudentId/{studentId}")
    public ResponseEntity<SuccessResponse<List<GetSupplyExamDetailsByStudentIdRes>>> getSupplyExamDetailsByStudentId(@PathVariable("studentId") String studentId) {
        List<GetSupplyExamDetailsByStudentIdRes> response = studentExamService.getSupplyExamDetailsByStudentId(studentId);
        return ResponseBuilder1.ok(
                SuccessMessages.SUPPLY_EXAM_DETAILS_FETCHED.getMessage(studentId),
                response
        );
    }

}
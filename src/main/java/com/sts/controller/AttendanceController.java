package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.dto.attendance.AddAttendanceRequest;
import com.sts.dto.attendance.AttendanceRequest;
import com.sts.dto.attendance.AttendanceResponse;
import com.sts.dto.attendance.AttendanceUpdateRequest;
import com.sts.dto.attendance.GetAttendanceByStudentIdAndSubjectCodeReq;
import com.sts.dto.attendance.GetStudentAllSemesterAttendanceRes;
import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;
import com.sts.service.interfaces.AttendanceService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping(Endpoints.V1_ATTENDANCES)
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/getAllStudentsSemesterAttendance/{subjectCode}")
    public ResponseEntity<?> getAllStudentsSemesterAttendance(@PathVariable("subjectCode") String subjectCode) {
        log.info("Fetching semester attendance for all students with subjectCode: {}", subjectCode);
        List<GetStudentSemesterAttendanceRes> response = attendanceService.getAllStudentsSemesterAttendance(subjectCode);
        return ResponseBuilder.ok(response, SuccessMessageEnum.ATTENDANCES_FETCHED, response.size());
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<?> getAttendancesByStudentId(@PathVariable("studentId") String studentId) {
        log.info("Fetching all semester attendance for studentId: {}", studentId);
        GetStudentAllSemesterAttendanceRes response = attendanceService.getAttendancesByStudentId(studentId);
        return ResponseBuilder.ok(response, SuccessMessageEnum.ATTENDANCE_FETCHED, studentId);
    }

    @PostMapping("/getAttendanceByStudentIdAndSubjectCode")
    public ResponseEntity<?> getAttendanceByStudentIdAndSubjectCode(@RequestBody GetAttendanceByStudentIdAndSubjectCodeReq req) {
        log.info("Fetching attendance for studentId: {} and subjectCode: {}", req.getStudentId(), req.getSubjectCode());
        String response = attendanceService.getAttendanceByStudentIdAndSubjectCode(req);
        return ResponseBuilder.ok(response, SuccessMessageEnum.ATTENDANCE_FETCHED, req.getStudentId(), req.getSubjectCode());
    }

    @PostMapping("/search")
    public ResponseEntity<?> getAttendance(@RequestBody AttendanceRequest filterRequest) {
        log.info("Searching attendance records with filter criteria: {}", filterRequest);
        List<AttendanceResponse> attendanceList = attendanceService.getAttendance(filterRequest);
        log.info("Found {} attendance records", attendanceList.size());
        return ResponseBuilder.ok(attendanceList, SuccessMessageEnum.ATTENDANCES_FETCHED, attendanceList.size());
    }

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody AttendanceRequest attendanceRequest) {
        log.info("Creating new attendance record: {}", attendanceRequest);
        AttendanceResponse savedAttendance = attendanceService.createAttendance(attendanceRequest);
        return ResponseBuilder.created(savedAttendance, SuccessMessageEnum.ATTENDANCE_CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateAttendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest) {
        log.info("Updating attendance record: {}", attendanceUpdateRequest);
        String updateStatus = attendanceService.updateAttendance(attendanceUpdateRequest);
        return ResponseBuilder.ok(updateStatus, SuccessMessageEnum.ATTENDANCE_UPDATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createSubjectStudentsAttendance(@RequestBody List<AttendanceRequest> attendanceRequests) {
        log.info("Creating bulk attendance records for {} students", attendanceRequests.size());
        String creationStatus = attendanceService.saveSubjectStudentsAttendance(attendanceRequests);
        return ResponseBuilder.created(creationStatus, SuccessMessageEnum.ATTENDANCES_BULK_CREATED, attendanceRequests.size());
    }

    @PostMapping("/bulk2")
    public ResponseEntity<?> bulkCreateAttendances(@RequestBody List<AddAttendanceRequest> requestList) {
        log.info("Creating multi-day attendance records for {} students", requestList.size());
        String creationStatus = attendanceService.bulkCreateAttendances(requestList);
        return ResponseBuilder.created(creationStatus, SuccessMessageEnum.ATTENDANCES_BULK_CREATED, requestList.size());
    }

    @PutMapping("/bulk")
    public ResponseEntity<?> updateMultipleAttendance(@RequestBody List<AttendanceUpdateRequest> attendanceUpdateRequests) {
        log.info("Updating bulk attendance records for {} students", attendanceUpdateRequests.size());
        String updateStatus = attendanceService.updateMultipleAttendance(attendanceUpdateRequests);
        return ResponseBuilder.ok(updateStatus, SuccessMessageEnum.ATTENDANCES_BULK_UPDATED, attendanceUpdateRequests.size());
    }
}
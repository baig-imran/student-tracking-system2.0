package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.attendance.AddAttendanceRequest;
import com.sts.dto.attendance.AttendanceRequest;
import com.sts.dto.attendance.AttendanceResponse;
import com.sts.dto.attendance.AttendanceUpdateRequest;
import com.sts.dto.attendance.GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes;
import com.sts.dto.attendance.GetActiveSemesterAttendanceByStudentIdRes;
import com.sts.dto.attendance.GetAttendanceByStudentIdAndSubjectCodeReq;
import com.sts.dto.attendance.GetStudentAllSemesterAttendanceRes;
import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;

public interface AttendanceService {
	

	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest);
	
	public String saveSubjectStudentsAttendance(List<AttendanceRequest> attendanceRequest);

	public AttendanceResponse createAttendance(AttendanceRequest attendanceRequest);

	public String updateMultipleAttendance(List<AttendanceUpdateRequest> attendanceUpdateRequests);

	public List<AttendanceResponse> getAttendance(AttendanceRequest filterRequest);

	String bulkCreateAttendances(List<AddAttendanceRequest> requestList);

	String getAttendanceByStudentIdAndSubjectCode(GetAttendanceByStudentIdAndSubjectCodeReq req);

	List<GetStudentSemesterAttendanceRes> getAllStudentsSemesterAttendance(String subjectCode);

	GetStudentAllSemesterAttendanceRes getAttendancesByStudentId(String studentId);

	GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes getActiveSemesterAttendanceByStudentIdAndSemesterCode(String studentId,
			String semesterCode);

	GetActiveSemesterAttendanceByStudentIdRes getActiveSemesterAttendanceByStudentId(String studentId);
	
	
	

}

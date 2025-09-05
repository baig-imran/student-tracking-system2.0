package com.sts.dto.attendance;

import java.util.List;

import lombok.Data;

@Data
public class GetStudentAllSemesterAttendanceRes {
	private String studentId;
	List<StudentSemesterSubjectAttendanceMap> semesterSubjectsAttendance;

	@Data
	public static class StudentSemesterSubjectAttendanceMap{

		private String semesterCode;
		private int semesterSerialNumber;
		private List<SubjectAttendance> semesterSubjectAttendanceList;
	}

}

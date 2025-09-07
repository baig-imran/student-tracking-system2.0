package com.sts.dto.mentoring;

import java.util.ArrayList;
import java.util.List;

import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;

import lombok.Data;

@Data
public class GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes {
	private String studentId;
	private String semesterCode;
	private double semesterAttendance;
	private List<SubjectAttendance> subjectAttendances = new ArrayList<>();
	
	@Data
	public static class SubjectAttendance{
		private String subjectCode;
		private String subjectShortForm;
		private int totalWorkingsDays;
		private double attendancePercentage;
		private int totalDaysPresent;
		private int totalDaysAbsent;
	}}

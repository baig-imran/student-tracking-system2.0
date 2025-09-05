package com.sts.dto.attendance;

import java.util.List;

import lombok.Data;

@Data
public class GetStudentSemesterAttendanceRes {
	private String studentId;
	private String subjectCode;
	private int totalWorkingsDays;
	private double attendancePercentage;
	private int totalDaysPresent;
	private int totalDaysAbsent;
	
}

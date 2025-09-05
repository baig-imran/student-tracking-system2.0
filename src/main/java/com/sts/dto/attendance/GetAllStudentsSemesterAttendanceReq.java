package com.sts.dto.attendance;

import lombok.Data;

@Data
public class GetAllStudentsSemesterAttendanceReq {
	private String studentId;
	private String subjectCode;
	private int totalWorkingsDays;
	private int present;
	private int absent;
	private double percentage; 
}

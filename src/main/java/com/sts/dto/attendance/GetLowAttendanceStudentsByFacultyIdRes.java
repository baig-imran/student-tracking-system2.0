package com.sts.dto.attendance;

import lombok.Data;

@Data
public class GetLowAttendanceStudentsByFacultyIdRes {
	 	private String studentId;
	    private String semesterCode;
	    private Long totalDaysPresent;
	    private Long totalWorkingDays;
	    private Double attendancePercentage;

}

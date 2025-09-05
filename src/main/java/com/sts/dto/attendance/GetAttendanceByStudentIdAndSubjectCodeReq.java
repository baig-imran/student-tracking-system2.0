package com.sts.dto.attendance;

import lombok.Data;

@Data
public class GetAttendanceByStudentIdAndSubjectCodeReq {
	private String studentId;
	private String subjectCode;

}

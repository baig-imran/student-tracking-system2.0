package com.sts.dto.exam.studentexam;

import lombok.Data;

@Data
public class GetLowExternalMarksStudentsByFacultyIdRes {
	private String studentId;
	private String studentName;
	private String subjectCode;
    private String subjectTitle;
    private String subjectShortForm;
	private String examCode;
	private Double marksObtained;
	private Double passMarks;

}

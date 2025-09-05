package com.sts.dto.mentoring;

import lombok.Data;

@Data
public class GetMentoringStudentsByFacultyIdRes {
	
	private String studentId;
    private String studentName;
    private String fatherName;
    private String fatherMobileNumber;
    private String studentMobileNumber;
    private Boolean isGraduated;
    private String regulation;
    private String batch;
    private String departmentId; 
    private String mentorId; 
}

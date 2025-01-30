package com.sts.dto;

import java.time.LocalDate;

import com.sts.entity.Semester;
import com.sts.entity.Student;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class AttendanceResponse {
	
	private String departmentId;
    private String subjectCode;
    private LocalDate attendanceDate; 
    private Integer period; 
    private Boolean isPresent;
    private String studentId;
    private String semesterCode;
    
}
    
    

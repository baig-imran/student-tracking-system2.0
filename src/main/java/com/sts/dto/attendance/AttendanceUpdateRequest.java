package com.sts.dto.attendance;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceUpdateRequest {
	
    private String subjectCode;
    private LocalDate attendanceDate; 
    private Integer period; 
    private Boolean isPresent;
    private String studentId;
    
}
    
    

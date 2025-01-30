package com.sts.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExamRequest {

    private String examCode; // Common code for all students
    private String examType;
    private String examName;
    private LocalDate examDate;

    private String subjectCode;
    private Double marksObtained;
    private String semesterCode; 
    private String studentId; 

}

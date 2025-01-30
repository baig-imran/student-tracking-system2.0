package com.sts.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExamResponse {

    private String examCode; // Common code for all students
    private String examType;
    private String examName;
    private LocalDate examDate;

    private String subjectCode;
    private Double marksObtained;
    private String semesterCode; // Reference to Semester entity
    private String studentId; // Reference to Student entity

}

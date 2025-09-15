package com.sts.dto.exam;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateExamByExmaCodeReq {

    private String examCode;
    private String examType;       
    private String examSubType;    
    private String examName;
    private LocalDate examDate;
    private Double passMarks;

    private String subjectCode;  
    private String semesterCode;
    
    
}
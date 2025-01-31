package com.sts.dto;

import lombok.Data;

@Data
public class ExamUpdateRequest {

    private String examName;
    private String subjectCode;
    private String studentId; 
    private Double marksObtained;
 
}

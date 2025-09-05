package com.sts.dto.student;

import lombok.Data;

@Data
public class StudentUpdateRequest {

    private String studentId;
    private String studentName;
    private String studentMobileNumber;
    private String fatherName;
    private String fatherMobileNumber;
    private Boolean isGraduated;
    private String batch;
    private String regulation;
    private String departmentId;
    private String mentorId;
}
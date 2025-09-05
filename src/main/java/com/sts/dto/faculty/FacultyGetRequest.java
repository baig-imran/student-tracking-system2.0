package com.sts.dto.faculty;

import lombok.Data;

@Data
public class FacultyGetRequest {
    private String facultyId;
    private String facultyName;
    private String facultyMobileNumber;
    private String departmentId;
}
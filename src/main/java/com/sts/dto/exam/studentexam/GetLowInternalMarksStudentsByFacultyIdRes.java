package com.sts.dto.exam.studentexam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLowInternalMarksStudentsByFacultyIdRes {
    private String studentId;
    private String studentName;
    private String semesterCode;
    private Double totalInternals;
    private Double totalExternals;
    private Double total;
    private Double sgpa;
    private String academicYear;
}

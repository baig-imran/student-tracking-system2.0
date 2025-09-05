package com.sts.dto.exam;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetExamsBySpecificationRes {

    private String examCode;
    private String examType;       // e.g., Internal/External
    private String examSubType;    // e.g., mid, supply
    private String examName;
    private LocalDate examDate;
    private Double passMarks;

    private String subjectCode;    // From SemesterSubject
    private String semesterCode;
}
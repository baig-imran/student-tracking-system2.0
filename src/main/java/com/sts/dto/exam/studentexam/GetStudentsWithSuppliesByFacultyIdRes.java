package com.sts.dto.exam.studentexam;

import lombok.Data;

@Data
public class GetStudentsWithSuppliesByFacultyIdRes {
    private String studentId;
    private String studentName;
    private int supplies;   // number of supply subjects
}

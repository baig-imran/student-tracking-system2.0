package com.sts.dto.exam.studentexam;

import lombok.Data;

@Data
public class GetStudentsWithSupplyByFacultyIdRes {
    private String studentId;
    private String studentName;
    private int supplyCount;
}

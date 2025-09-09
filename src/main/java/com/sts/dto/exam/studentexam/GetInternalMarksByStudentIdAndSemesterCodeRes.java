package com.sts.dto.exam.studentexam;

import lombok.Data;

@Data
public class GetInternalMarksByStudentIdAndSemesterCodeRes {

    private String subjectCode;
    private String subjectTitle;
    private String subjectShortForm;
    private Double internals;
    private Double externals;
    private Double total;
    private Integer credits;
    private Integer gpa;
    private String passDate;
}
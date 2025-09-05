package com.sts.dto.exam.studentexam;

import java.util.List;

import lombok.Data;

@Data
public class AddMultipleSubjectExamDataUploadReq {
    private List<AddStudentsExamDataReq> examsData;
}
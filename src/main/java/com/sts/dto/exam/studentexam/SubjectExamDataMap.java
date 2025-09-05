package com.sts.dto.exam.studentexam;

import java.util.List;

import lombok.Data;

@Data
public class SubjectExamDataMap {
	private String examCode;
    private List<AddStudentsExamDataReq> studentsExamData;

}


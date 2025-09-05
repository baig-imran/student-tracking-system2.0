package com.sts.dto.exam.studentexam;

import java.util.List;

import com.sts.entity.StudentExam;

import lombok.Data;

@Data
public class AddStudentsExamDataRes {
	
	private String examCode;
	private List<String> studentIds;
	
   

}

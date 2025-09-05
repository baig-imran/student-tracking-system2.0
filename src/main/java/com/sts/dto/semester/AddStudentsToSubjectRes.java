package com.sts.dto.semester;

import java.util.List;

import com.sts.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AddStudentsToSubjectRes {
	//private String semesterCode;
	private String subjectCode;
	private String studentId;

}

package com.sts.dto.exam.studentexam;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GetStudentAllSemesterExamDetailsRes {
	private String studentId;
	private Map<Integer, List<SubjectExamSummary>> semesters; // semesterSerialNumber → List of subjects


	@Data
	public static class SubjectExamSummary {
		private String subjectCode;
		private Double assignments; // Sum of all assignment exams
		private Double mids;        // Sum of all mid exams
		private Double total;       // assignments + mids
	}

}
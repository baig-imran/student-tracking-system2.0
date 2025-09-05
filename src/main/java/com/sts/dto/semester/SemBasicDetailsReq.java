package com.sts.dto.semester;

import java.time.LocalDate;
import java.util.List;

import com.sts.entity.Faculty;
import com.sts.entity.Student;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class SemBasicDetailsReq {

	private String semesterCode;
	private Integer semesterNumber; //1 or 2
	private Integer semesterSerialNumber; // 1, 2...8

	private Integer studyYear;	// 1,2,3,4
	private Integer academicYear;//2020, 2021 ...
	private String regulation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String batch;
	private String departmentId;
}
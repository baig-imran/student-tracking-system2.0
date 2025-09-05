package com.sts.dto.faculty;


import lombok.Data;

@Data
public class FacultySemesterSubjectStudentsReq {
	String facultyId;
    String semesterCode;
    String subjectCode;

}

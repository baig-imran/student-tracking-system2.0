package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.semestersubject.FacultySemesterSubjectStudentsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse2;
import com.sts.entity.SemesterSubject;
import com.sts.entity.StudentSubject;

public interface SemesterSubjectService {



	FacultySemesterSubjectsGetResponse getActiveSemesterSubjectsByFaculty(FacultySemesterSubjectsGetRequest req);

	List<String> getActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(FacultySemesterSubjectStudentsGetRequest req);

	FacultySemesterSubjectsGetResponse2 getActiveSemesterSubjectsByFacultyId(String facultyId);
	

	
	
	

}

package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.StudentCreateRequest;
import com.sts.dto.StudentGetRequest;
import com.sts.dto.StudentResponse;

public interface StudentService{
	
	StudentResponse saveStudent(StudentCreateRequest studentCreateRequest);

	String saveMultipleStudents(List<StudentCreateRequest> studentCreateRequests);

	List<StudentResponse> getStudentsByCriteria(StudentGetRequest studentGetRequest);
	
	
	
	


}

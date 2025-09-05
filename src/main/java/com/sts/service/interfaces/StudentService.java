package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.student.GetStudentsByIdsReq;
import com.sts.dto.student.StudentCreateRequest;
import com.sts.dto.student.StudentGetRequest;
import com.sts.dto.student.StudentResponse;
import com.sts.dto.student.StudentUpdateRequest;

public interface StudentService{
	
	StudentResponse saveStudent(StudentCreateRequest studentCreateRequest);

	String saveMultipleStudents(List<StudentCreateRequest> studentCreateRequests);

	List<StudentResponse> getStudentsByCriteria(StudentGetRequest studentGetRequest);

	StudentResponse updateStudent(StudentUpdateRequest studentRequest);
	StudentResponse getStudentById(String studentId);

	String deleteById(String studentId);

	List<StudentGetRequest> getStudentsByStudentIds(GetStudentsByIdsReq req);

	String updateBulkStudents(List<StudentUpdateRequest> reqs);

	List<String> deleteByIds(List<String> req);
	
	
	
	
	
	
	


}

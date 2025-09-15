package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.student.GetStudentsByIdsReq;
import com.sts.dto.student.CreateStudentReq;
import com.sts.dto.student.GetStudentReq;
import com.sts.dto.student.StudentResponse;
import com.sts.dto.student.UpdateStudentReq;

public interface StudentService{
	
	StudentResponse saveStudent(CreateStudentReq createStudentReq);

	String createBulkStudents(List<CreateStudentReq> createStudentReqs);

	List<StudentResponse> getStudentsBySpecification(GetStudentReq getStudentReq);

	StudentResponse updateStudent(UpdateStudentReq studentRequest);
	StudentResponse getStudentById(String studentId);

	String deleteById(String studentId);

	List<GetStudentReq> getStudentsByStudentIds(GetStudentsByIdsReq req);

	String updateBulkStudents(List<UpdateStudentReq> reqs);

	List<String> deleteByIds(List<String> req);
	
	
	
	
	
	
	


}

package com.sts.service.interfaces;

import com.sts.dto.StudentResponse;
import com.sts.dto.StudentRequest;

public interface StudentService{
	
	StudentResponse saveStudent(StudentRequest studentRequest);
	
	


}

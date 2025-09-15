package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.faculty.FacultyCreateRequest;
import com.sts.dto.faculty.FacultyGetRequest;
import com.sts.dto.faculty.FacultyResponse;
import com.sts.dto.faculty.FacultyUpdateRequest;

public interface FacultyService {
    FacultyResponse getFacultyById(String facultyId);
    List<FacultyResponse> getAllFaculties();
    FacultyResponse createFaculty(FacultyCreateRequest request);
    FacultyResponse updateFaculty(FacultyUpdateRequest request);
    String deleteFacultyById(String facultyId);
	List<FacultyResponse> getFacultyBySpecification(FacultyGetRequest facultyGetRequest);
	List<FacultyResponse> saveMultipleFaculties(List<FacultyCreateRequest> facultyRequests);
	String addBulkFaculties(List<FacultyCreateRequest> requests);
	
}


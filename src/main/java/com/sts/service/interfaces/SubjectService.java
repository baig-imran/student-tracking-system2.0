package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.subjects.SubjectCreateRequest;
import com.sts.dto.subjects.SubjectGetRequest;
import com.sts.dto.subjects.SubjectResponse;
import com.sts.dto.subjects.SubjectUpdateRequest;

public interface SubjectService {
    SubjectResponse getSubjectById(String subjectId);
    List<SubjectResponse> getAllSubjects();
    SubjectResponse saveSubject(SubjectCreateRequest request);
    SubjectResponse updateSubject(SubjectUpdateRequest request);
    String deleteSubjectById(String subjectId);
	List<SubjectResponse> getSubjectsByCriteria(SubjectGetRequest subjectGetRequest);
	List<SubjectResponse> saveMultipleSubjects(List<SubjectCreateRequest> subjectRequests);
	
}


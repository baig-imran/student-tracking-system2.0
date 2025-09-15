package com.sts.service.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.EntityNames;
import com.sts.constants.ErrorMessages;
import com.sts.constants.ServiceLogDebugMessages;
import com.sts.constants.ServiceLogInfoMessages;
import com.sts.dto.subjects.SubjectCreateRequest;
import com.sts.dto.subjects.SubjectGetRequest;
import com.sts.dto.subjects.SubjectResponse;
import com.sts.dto.subjects.SubjectUpdateRequest;
import com.sts.entity.Department;
import com.sts.entity.Subjects;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.SubjectRepository;
import com.sts.service.interfaces.SubjectService;
import com.sts.specification.SubjectsSpecification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public SubjectServiceImpl(SubjectRepository subjectRepository,
                               DepartmentRepository departmentRepository,
                               ModelMapper modelMapper) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubjectResponse getSubjectById(String subjectId) {
    	log.info(ServiceLogInfoMessages.FETCHING_ENTITY_WITH_ID
				.getMessage(EntityNames.SUBJECT.getName(), subjectId));
        Subjects subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
        
        SubjectResponse response = modelMapper.map(subject, SubjectResponse.class);
        response.setDepartmentId(subject.getDepartment().getDepartmentId());
        log.info(ServiceLogInfoMessages.ENTITY_FETCHED_WITH_ID
				.getMessage(EntityNames.SUBJECT.getName(), response.getSubjectId()));
        return response;
    }
    
    @Override
    public List<SubjectResponse> getSubjectsBySpecification(SubjectGetRequest req) {
    	log.info(ServiceLogInfoMessages.FETCHING_ENTITIES_BY_SPECIFICATION.getMessage(EntityNames.SUBJECT.getName()));
        log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.SUBJECT.getName(), req));
    	List<Subjects> subjectList = subjectRepository.findAll(SubjectsSpecification.getSubjectSpec(req));

        if (subjectList.isEmpty()) {
            log.error("No subjects found for the given specification");
            throw new ResourceNotFoundException(ErrorMessages.SUBJECTS_NOT_FOUND.getMessage());
        }

        List<SubjectResponse> responseList = subjectList.stream()
                .map(subjects -> {
                    SubjectResponse response = modelMapper.map(subjects, SubjectResponse.class);
                    // Manually set the departmentId from the nested Department object
                    if (subjects.getDepartment() != null) {
                        response.setDepartmentId(subjects.getDepartment().getDepartmentId());
                    }
                    return response;
                })
                .collect(Collectors.toList());

        log.info("Successfully fetched {} subjects.", responseList.size());
        return responseList;
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
    	 log.info(ServiceLogInfoMessages.FETCHING_ALL_ENTITIES.getMessage(EntityNames.SUBJECTS.getName()));
        return subjectRepository.findAll().stream()
            .map(subject -> {
                SubjectResponse response = modelMapper.map(subject, SubjectResponse.class);
                response.setDepartmentId(subject.getDepartment().getDepartmentId());
                return response;
            })
            .collect(Collectors.toList());
    }

    @Override
    public SubjectResponse response(SubjectCreateRequest request) {
        Subjects subject = modelMapper.map(request, Subjects.class);
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartmentId()));
        
        subject.setDepartment(department);
        Subjects saved = subjectRepository.save(subject);

        SubjectResponse response = modelMapper.map(saved, SubjectResponse.class);
        response.setDepartmentId(department.getDepartmentId());
        return response;
    }
    
    @Override
    public List<SubjectResponse> createBulkSubjects(List<SubjectCreateRequest> subjectRequests) {
        log.info("Starting to save multiple subjects, total records: {}", subjectRequests.size());

        // TODO: Validate each subject request (if you have a validateSubjectRequest method)
        // TODO: subjectRequests.forEach(this::validateSubjectRequest);

        List<Subjects> subjects = subjectRequests.stream()
            .map(request -> {
                Subjects subject = modelMapper.map(request, Subjects.class);
                Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartmentId()));
                subject.setDepartment(department);
                return subject;
            })
            .collect(Collectors.toList());

        List<Subjects> savedSubjects = subjectRepository.saveAll(subjects);

        List<SubjectResponse> responses = savedSubjects.stream()
            .map(saved -> {
                SubjectResponse response = modelMapper.map(saved, SubjectResponse.class);
                response.setDepartmentId(saved.getDepartment().getDepartmentId());
                return response;
            })
            .collect(Collectors.toList());

        log.info("Successfully saved {} subjects.", responses.size());
        return responses;
    }


    @Override
    public SubjectResponse updateSubject(SubjectUpdateRequest request) {
        Subjects existing = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));
        
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartmentId()));
        
        existing.setSubjectTitle(request.getSubjectTitle());
        existing.setSubjectShortForm(request.getSubjectShortForm());
        existing.setCredits(request.getCredits());
        existing.setDepartment(department);

        Subjects updated = subjectRepository.save(existing);

        SubjectResponse response = modelMapper.map(updated, SubjectResponse.class);
        response.setDepartmentId(department.getDepartmentId());
        return response;
    }

    @Override
    public String deleteSubjectById(String subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException("Subject not found: " + subjectId);
        }
        subjectRepository.deleteById(subjectId);
        return "Subject deleted successfully: " + subjectId;
    }
}

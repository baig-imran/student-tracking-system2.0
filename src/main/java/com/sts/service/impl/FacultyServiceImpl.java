package com.sts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.EntityNames;
import com.sts.constants.ErrorMessages;
import com.sts.constants.ServiceLogDebugMessages;
import com.sts.constants.ServiceLogInfoMessages;
import com.sts.constants.SuccessMessages;
import com.sts.dto.faculty.FacultyCreateRequest;
import com.sts.dto.faculty.FacultyGetRequest;
import com.sts.dto.faculty.FacultyResponse;
import com.sts.dto.faculty.FacultyUpdateRequest;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.service.interfaces.FacultyService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              DepartmentRepository departmentRepository,
                              ModelMapper modelMapper) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FacultyResponse getFacultyById(String facultyId) {
        log.info(ServiceLogInfoMessages.FETCHING_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY.getName(), facultyId));
        
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(facultyId)));
        
        FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
        response.setDepartmentId(faculty.getDepartment().getDepartmentId());
        
        log.info(ServiceLogInfoMessages.ENTITY_FETCHED_WITH_ID.getMessage(EntityNames.FACULTY.getName(), facultyId));
        return response;
    }
    
    @Override
	public List<FacultyResponse> getFacultyBySpecification(FacultyGetRequest facultyGetRequest) {
    	log.info(ServiceLogInfoMessages.FETCHING_ENTITIES_BY_SPECIFICATION.getMessage(EntityNames.FACULTY.getName()));
        log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTY.getName(), facultyGetRequest));
    	
    	List<FacultyResponse> facultyList =new ArrayList<>();
    	Faculty faculty = facultyRepository.findById(facultyGetRequest.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(facultyGetRequest.getFacultyId())));
        FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
        response.setDepartmentId(faculty.getDepartment().getDepartmentId());
        
        facultyList.add(response);
        
        log.info(ServiceLogInfoMessages.ENTITIES_FETCHED_BY_SPECIFICATION.getMessage(facultyList.size(), EntityNames.FACULTY.getName()));
		return facultyList;
	}

    @Override
    public List<FacultyResponse> getAllFaculties() {
        log.info(ServiceLogInfoMessages.FETCHING_ALL_ENTITIES.getMessage(EntityNames.FACULTIES.getName()));

        List<Faculty> facultyList = facultyRepository.findAll();
        
        if(facultyList.isEmpty()){
            log.warn("No faculties found in the system.");
            throw new ResourceNotFoundException(ErrorMessages.FACULTY_REQUIRED.getMessage());
        }

        List<FacultyResponse> responses = facultyList.stream()
                .map(faculty -> {
                    FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
                    response.setDepartmentId(faculty.getDepartment().getDepartmentId());
                    return response;
                })
                .collect(Collectors.toList());
        
        log.info(ServiceLogInfoMessages.ALL_ENTITIES_FETCHED.getMessage(responses.size(), EntityNames.FACULTIES.getName()));
        return responses;
    }

    @Override
    public FacultyResponse createFaculty(FacultyCreateRequest request) {
        log.info(ServiceLogInfoMessages.CREATING_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY.getName(), request.getFacultyId()));
        log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTY.getName(), request));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId())));

        Faculty faculty = modelMapper.map(request, Faculty.class);
        faculty.setDepartment(department);

        Faculty saved = facultyRepository.save(faculty);
        FacultyResponse response = modelMapper.map(saved, FacultyResponse.class);
        response.setDepartmentId(saved.getDepartment().getDepartmentId());
        
        log.info(ServiceLogInfoMessages.ENTITY_CREATED_WITH_ID.getMessage(EntityNames.FACULTY.getName(), response.getFacultyId()));
        return response;
    }
    
    @Override
    @Transactional
    public String addBulkFaculties(List<FacultyCreateRequest> requests) {
        log.info(ServiceLogInfoMessages.CREATING_BULK_ENTITIES_WITH_SIZE.getMessage(EntityNames.FACULTY.getName(), requests.size()));
        log.debug(ServiceLogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.FACULTY.getName(), requests.size(), requests));

        // Step 1: Extract unique department IDs
        Set<String> departmentIds = requests.stream()
                .map(FacultyCreateRequest::getDepartmentId)
                .collect(Collectors.toSet());

        // Step 2: Fetch all relevant departments
        Map<String, Department> departmentMap = departmentRepository.findAllById(departmentIds).stream()
                .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));
        
        // Step 3: Map requests to Faculty entities
        List<Faculty> faculties = requests.stream()
                .map(request -> {
                    Department department = departmentMap.get(request.getDepartmentId());
                    if (department == null) {
                        log.error(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId()));
                        throw new ResourceNotFoundException(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId()));
                    }

                    Faculty faculty = modelMapper.map(request, Faculty.class);
                    faculty.setDepartment(department);
                    return faculty;
                })
                .collect(Collectors.toList());

        // Step 4: Save all faculty entries
        List<Faculty> savedFaculties = facultyRepository.saveAll(faculties);

        // Step 5: Map to response DTOs
        List<FacultyResponse> responses = savedFaculties.stream()
                .map(faculty -> {
                    FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
                    response.setDepartmentId(faculty.getDepartment().getDepartmentId());
                    return response;
                })
                .collect(Collectors.toList());

        log.info(ServiceLogInfoMessages.BULK_ENTITIES_CREATED.getMessage(responses.size(), EntityNames.FACULTY.getName()));
        return SuccessMessages.BULK_ENTITIES_CREATED.getMessage(responses.size(), EntityNames.FACULTIES.getName());
    }

    
    @Override
    public List<FacultyResponse> saveMultipleFaculties(List<FacultyCreateRequest> facultyRequests) {
        log.info(ServiceLogInfoMessages.CREATING_BULK_ENTITIES_WITH_SIZE.getMessage(EntityNames.FACULTY.getName(), facultyRequests.size()));
        log.debug(ServiceLogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.FACULTY.getName(), facultyRequests.size(), facultyRequests));

        List<Faculty> faculties = facultyRequests.stream()
            .map(request -> {
                Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId())));

                Faculty faculty = modelMapper.map(request, Faculty.class);
                faculty.setDepartment(department);
                return faculty;
            })
            .collect(Collectors.toList());

        List<Faculty> savedFaculties = facultyRepository.saveAll(faculties);

        List<FacultyResponse> responses = savedFaculties.stream()
            .map(faculty -> {
                FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
                response.setDepartmentId(faculty.getDepartment().getDepartmentId());
                return response;
            })
            .collect(Collectors.toList());

        log.info(ServiceLogInfoMessages.BULK_ENTITIES_CREATED.getMessage(responses.size(), EntityNames.FACULTY.getName()));
        return responses;
    }


    @Override
    public FacultyResponse updateFaculty(FacultyUpdateRequest request) {
        log.info(ServiceLogInfoMessages.UPDATING_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY.getName(), request.getFacultyId()));
        log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.FACULTY.getName(), request));

        Faculty existingFaculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(request.getFacultyId())));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId())));

        existingFaculty.setFacultyName(request.getFacultyName());
        existingFaculty.setFacultyMobileNumber(request.getFacultyMobileNumber());
        existingFaculty.setDepartment(department);

        Faculty updated = facultyRepository.save(existingFaculty);
        FacultyResponse response = modelMapper.map(updated, FacultyResponse.class);
        response.setDepartmentId(updated.getDepartment().getDepartmentId());

        log.info(ServiceLogInfoMessages.ENTITY_UPDATED_WITH_ID.getMessage(EntityNames.FACULTY.getName(), response.getFacultyId()));
        return response;
    }

    @Override
    public String deleteFacultyById(String facultyId) {
        log.info(ServiceLogInfoMessages.DELETING_ENTITY_WITH_ID.getMessage(EntityNames.FACULTY.getName(), facultyId));
        
        if (!facultyRepository.existsById(facultyId)) {
            log.error(ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(facultyId));
            throw new ResourceNotFoundException(ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(facultyId));
        }

        facultyRepository.deleteById(facultyId);
        
        log.info(ServiceLogInfoMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.FACULTY.getName(), facultyId));
        return SuccessMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.FACULTY.getName(), facultyId);
    }
}
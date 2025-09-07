package com.sts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID " + facultyId + " not found"));
        FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
        response.setDepartmentId(faculty.getDepartment().getDepartmentId());
        return response;
    }
    
    @Override
	public List<FacultyResponse> getFacultyByCriteria(FacultyGetRequest facultyGetRequest) {
		 
    	List<FacultyResponse> facultyList =new ArrayList<>();
    	Faculty faculty = facultyRepository.findById(facultyGetRequest.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID " + facultyGetRequest.getFacultyId() + " not found"));
        FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
        
        facultyList.add(response);
		return facultyList;
	}

    @Override
    public List<FacultyResponse> getAllFaculties() {
        List<Faculty> facultyList = facultyRepository.findAll();
        return facultyList.stream()
                .map(faculty -> {
                    FacultyResponse response = modelMapper.map(faculty, FacultyResponse.class);
                    response.setDepartmentId(faculty.getDepartment().getDepartmentId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FacultyResponse saveFaculty(FacultyCreateRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + request.getDepartmentId() + " not found"));

        Faculty faculty = modelMapper.map(request, Faculty.class);
        faculty.setDepartment(department);

        Faculty saved = facultyRepository.save(faculty);
        FacultyResponse response = modelMapper.map(saved, FacultyResponse.class);
        response.setDepartmentId(saved.getDepartment().getDepartmentId());
        return response;
    }
    
    @Override
    @Transactional
    public String addBulkFaculties(List<FacultyCreateRequest> requests) {
        log.info("Starting bulk faculty creation. Total records: {}", requests.size());

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
                        throw new ResourceNotFoundException("Department with ID " + request.getDepartmentId() + " not found");
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

        log.info("Successfully created {} faculty records.", responses.size());
        return "Successfully created "+ responses.size()+" faculty records.";
    }

    
    @Override
    public List<FacultyResponse> saveMultipleFaculties(List<FacultyCreateRequest> facultyRequests) {
        log.info("Starting to save multiple faculties, total records: {}", facultyRequests.size());

        List<Faculty> faculties = facultyRequests.stream()
            .map(request -> {
                Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + request.getDepartmentId() + " not found"));

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

        log.info("Successfully saved {} faculties.", responses.size());
        return responses;
    }


    @Override
    public FacultyResponse updateFaculty(FacultyUpdateRequest request) {
        Faculty existingFaculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty with ID " + request.getFacultyId() + " not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + request.getDepartmentId() + " not found"));

        existingFaculty.setFacultyName(request.getFacultyName());
        existingFaculty.setFacultyMobileNumber(request.getFacultyMobileNumber());
        existingFaculty.setDepartment(department);

        Faculty updated = facultyRepository.save(existingFaculty);
        FacultyResponse response = modelMapper.map(updated, FacultyResponse.class);
        response.setDepartmentId(updated.getDepartment().getDepartmentId());
        return response;
    }

    @Override
    public String deleteFacultyById(String facultyId) {
        if (!facultyRepository.existsById(facultyId)) {
            throw new ResourceNotFoundException("Faculty with ID " + facultyId + " not found");
        }

        facultyRepository.deleteById(facultyId);
        return "Faculty with ID " + facultyId + " deleted successfully";
    }

	
}

package com.sts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.department.DepartmentCreateRequest;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.DepartmentUpdateRequest;
import com.sts.entity.Department;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.DepartmentService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 StudentRepository studentRepository,
                                 FacultyRepository facultyRepository,
                                 ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentResponse getDepartmentById(String departmentId) {
        log.info("Fetching department with ID: {}", departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(departmentId)));

        DepartmentResponse response = mapToDepartmentResponse(department);

        log.info("Successfully fetched department with ID: {}", departmentId);
        return response;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        log.info("Fetching all departments");

        List<Department> departments = departmentRepository.findAll();

        if (departments.isEmpty()) {
            log.warn("No departments found in the system.");
            throw new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_REQUIRED.getMessage());
        }

        List<DepartmentResponse> responses = departments.stream()
                .map(this::mapToDepartmentResponse)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} departments.", responses.size());
        return responses;
    }

    @Override
    @Transactional
    public DepartmentResponse saveDepartment(DepartmentCreateRequest request) {
        log.info("Starting to save department with request: {}", request);

        Department newDepartment = modelMapper.map(request, Department.class);

        Department savedDepartment = departmentRepository.save(newDepartment);

        DepartmentResponse response = mapToDepartmentResponse(savedDepartment);

        log.info("Department saved successfully with ID: {}", savedDepartment.getDepartmentId());
        return response;
    }

    @Override
    @Transactional
    public String bulkCreateDepartments(List<DepartmentCreateRequest> requests) {
        log.info("Starting to save multiple departments, total records: {}", requests.size());

        List<Department> departments = requests.stream()
                .map(req -> modelMapper.map(req, Department.class))
                .collect(Collectors.toList());

        departmentRepository.saveAll(departments);

        log.info("Successfully saved {} departments.", departments.size());
        return "Successfully saved " + departments.size() + " departments";
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(DepartmentUpdateRequest request) {
        log.info("Starting to update department with request: {}", request);

        Department existingDepartment = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId())));

        modelMapper.map(request, existingDepartment);

        Department savedDepartment = departmentRepository.save(existingDepartment);

        DepartmentResponse response = mapToDepartmentResponse(savedDepartment);

        log.info("Department updated successfully with ID: {}", savedDepartment.getDepartmentId());
        return response;
    }

    @Override
    public String deleteDepartmentById(String departmentId) {
        log.info("Attempting to delete department with ID: {}", departmentId);

        if (!departmentRepository.existsById(departmentId)) {
            log.warn("Department ID {} not found for deletion", departmentId);
            throw new ResourceNotFoundException(
                    ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(departmentId));
        }

        departmentRepository.deleteById(departmentId);

        log.info("Department with ID {} deleted successfully", departmentId);
        return "Department with ID " + departmentId + " deleted successfully";
    }

    // ==============================
    // Helper Method
    // ==============================
    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return modelMapper.map(department, DepartmentResponse.class);
    }
}

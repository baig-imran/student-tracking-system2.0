package com.sts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.EntityNames;
import com.sts.constants.ErrorMessages;
import com.sts.constants.ServiceLogDebugMessages;
import com.sts.constants.ServiceLogInfoMessages;
import com.sts.constants.SuccessMessages;
import com.sts.dto.department.CreateDepartmentReq;
import com.sts.dto.department.CreateDepartmentRes;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.GetDepartmentByIdRes;
import com.sts.dto.department.UpdateDepartmentReq;
import com.sts.dto.department.UpdateDepartmentRes;
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

	// Constructor Injection (cleaner than field injection)
	public DepartmentServiceImpl(DepartmentRepository departmentRepository,
			StudentRepository studentRepository,
			FacultyRepository facultyRepository,
			ModelMapper modelMapper) {
		this.departmentRepository = departmentRepository;
		this.studentRepository = studentRepository;
		this.facultyRepository = facultyRepository;
		this.modelMapper = modelMapper;
	}

	/**
	 * Fetch a department by its ID.
	 */
	@Override
	public GetDepartmentByIdRes getDepartmentById(String departmentId) {
		log.info(ServiceLogInfoMessages.FETCHING_ENTITY_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), departmentId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(departmentId)));

		log.info(ServiceLogInfoMessages.ENTITY_FETCHED_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), departmentId));

		return modelMapper.map(department, GetDepartmentByIdRes.class);
	}

	/**
	 * Fetch all departments.
	 */
	@Override
	public List<DepartmentResponse> getAllDepartments() {
		log.info(ServiceLogInfoMessages.FETCHING_ALL_ENTITIES
				.getMessage(EntityNames.DEPARTMENTS.getName()));

		List<Department> departments = departmentRepository.findAll();

		if (departments.isEmpty()) {
			log.warn("No departments found in the system.");
			throw new ResourceNotFoundException(ErrorMessages.DEPARTMENT_REQUIRED.getMessage());
		}

		List<DepartmentResponse> responses = departments.stream()
				.map(dept -> modelMapper.map(dept, DepartmentResponse.class))
				.collect(Collectors.toList());

		log.info(ServiceLogInfoMessages.ALL_ENTITIES_FETCHED
				.getMessage(responses.size(), EntityNames.DEPARTMENTS.getName()));

		return responses;
	}

	/**
	 * Create a new department.
	 */
	@Override
	@Transactional
	public CreateDepartmentRes createDepartment(CreateDepartmentReq req) {
		log.info(ServiceLogInfoMessages.CREATING_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT.getName(), req.getDepartmentId()));
		log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENT.getName(),req));
		Department savedDepartment = departmentRepository.save(modelMapper.map(req, Department.class));
		CreateDepartmentRes response = modelMapper.map(savedDepartment, CreateDepartmentRes.class);
		log.info(ServiceLogInfoMessages.ENTITY_CREATED_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), response.getDepartmentId()));
		return response;
	}

	/**
	 * Create multiple departments in bulk.
	 */
	@Override
	@Transactional
	public String createDepartmentsInBulk(List<CreateDepartmentReq> req) {
		log.info(ServiceLogInfoMessages.CREATING_BULK_ENTITIES_WITH_SIZE
				.getMessage(EntityNames.DEPARTMENT.getName(), req.size()));
		log.debug(ServiceLogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENT.getName(),req.size(),req));
		List<Department> departments = req.stream()
				.map(dept -> modelMapper.map(dept, Department.class))
				.collect(Collectors.toList());

		departmentRepository.saveAll(departments);

		log.info(ServiceLogInfoMessages.BULK_ENTITIES_CREATED
				.getMessage(departments.size(), EntityNames.DEPARTMENT.getName()));

		return SuccessMessages.BULK_ENTITIES_CREATED
				.getMessage(req.size(), EntityNames.DEPARTMENTS.getName());
	}

	/**
	 * Update an existing department.
	 */
	@Override
	@Transactional
	public UpdateDepartmentRes updateDepartment(UpdateDepartmentReq req) {
		log.info(ServiceLogInfoMessages.UPDATING_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT.getName(), req.getDepartmentId()));
		log.debug(ServiceLogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENT.getName(),req));
		Department existingDepartment = departmentRepository.findById(req.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(req.getDepartmentId())));
		modelMapper.map(req, existingDepartment);
		Department savedDepartment = departmentRepository.save(existingDepartment);
		UpdateDepartmentRes response = modelMapper.map(savedDepartment, UpdateDepartmentRes.class);
		log.info(ServiceLogInfoMessages.ENTITY_UPDATED_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), req.getDepartmentId()));
		return response;
	}

	/**
	 * Delete a department by its ID.
	 */
	@Override
	public String deleteDepartmentById(String departmentId) {
		log.info(ServiceLogInfoMessages.DELETING_ENTITY_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), departmentId));

		if (!departmentRepository.existsById(departmentId)) {
			log.error(ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(departmentId));
			throw new ResourceNotFoundException(
					ErrorMessages.DEPARTMENT_ID_NOT_FOUND.getMessage(departmentId));
		}
		departmentRepository.deleteById(departmentId);
		log.info(ServiceLogInfoMessages.ENTITY_DELETED_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), departmentId));
		return SuccessMessages.ENTITY_DELETED_WITH_ID
				.getMessage(EntityNames.DEPARTMENT.getName(), departmentId);
	}
}

package com.sts.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.dto.StudentCreateRequest;
import com.sts.dto.StudentGetRequest;
import com.sts.dto.StudentResponse;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Student;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.StudentService;
import com.sts.specification.StudentSpecification;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final ApplicationContext applicationContext;
	private final ValidatorRuleStatus validatorRuleStatus;

	public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleStatus) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.applicationContext = applicationContext;
		this.validatorRuleStatus = validatorRuleStatus;
	}

	@Override
	public List<StudentResponse> getStudents(StudentGetRequest studentGetRequest) {
		log.info("Fetching students based on filter criteria: {}", studentGetRequest);

		List<Student> studentList = studentRepository.findAll(StudentSpecification.getStudentSpec(studentGetRequest));

		if (studentList.isEmpty()) {
			log.info("No students found for the given filter criteria.");
			return Collections.emptyList();
		}

		List<StudentResponse> responseList = studentList.stream()
				.map(student -> {
					StudentResponse response = modelMapper.map(student, StudentResponse.class);
					response.setDepartmentId(student.getDepartment().getDepartmentId());
					response.setMentorId(student.getFaculty().getFacultyId());
					return response;
				})
				.collect(Collectors.toList());

		log.info("Successfully fetched {} students.", responseList.size());
		return responseList;
	}

	@Override
	public StudentResponse saveStudent(StudentCreateRequest studentRequest) {
		log.info("Starting to save student with request: {}", studentRequest);
		validateStudentRequest(studentRequest);

		Student newStudent = modelMapper.map(studentRequest, Student.class);

		log.info("Fetching department with ID: {}", studentRequest.getDepartmentId());
		Department department = departmentRepository.findById(studentRequest.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(studentRequest.getDepartmentId())));

		log.info("Fetching faculty with ID: {}", studentRequest.getMentorId());
		Faculty faculty = facultyRepository.findById(studentRequest.getMentorId())
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(studentRequest.getMentorId())));

		newStudent.setDepartment(department);
		newStudent.setFaculty(faculty);

		Student savedStudent = studentRepository.save(newStudent);

		StudentResponse studentResponse = modelMapper.map(savedStudent, StudentResponse.class);
		studentResponse.setDepartmentId(savedStudent.getDepartment().getDepartmentId());
		studentResponse.setMentorId(savedStudent.getFaculty().getFacultyId());

		log.info("Student saved successfully with ID: {}", savedStudent.getStudentId());
		return studentResponse;
	}



	@Override
	public String saveMultipleStudents(List<StudentCreateRequest> studentRequests) {
		log.info("Starting to save multiple students, total records: {}", studentRequests.size());

		studentRequests.forEach(this::validateStudentRequest);

		List<Student> students = studentRequests.stream()
				.map(request -> {
					Student student = modelMapper.map(request, Student.class);
					student.setDepartment(departmentRepository.findById(request.getDepartmentId())
							.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId()))));
					student.setFaculty(facultyRepository.findById(request.getMentorId())
							.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage( request.getMentorId()))));
					return student;
				})
				.collect(Collectors.toList());

		studentRepository.saveAll(students);
		log.info("Successfully saved {} students.", students.size());
		return "Successfully saved " + students.size() + " students";
	}
	
	private void validateStudentRequest(StudentCreateRequest studentRequest) {
		
		if (validatorRuleStatus.isRuleActive(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
			log.info("Validation rule '{}' is active, starting validation for student request.", ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
			Validator<StudentCreateRequest> validator = (Validator<StudentCreateRequest>) applicationContext.getBean(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getValidatorClass());
			validator.validate(studentRequest);
		}
	}
}

package com.sts.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.StudentCreateRequest;
import com.sts.dto.StudentGetRequest;
import com.sts.dto.StudentResponse;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Student;
import com.sts.exceptions.CustomException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.StudentRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.StudentService;
import com.sts.specification.StudentSpecification;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
	
	private String studentValidationRules = "STUDENT_VALIDATOR,FACULTY_VALIDATOR,DEPARTMENT_VALIDATOR,EXAM_VALIDATOR,ATTENDANCE_VALIDATOR,SUBJECT_VALIDATOR,SUBJECT_SEMESTER_VALIDATOR,SEMESTER_VALIDATOR";
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

	    try {
	        // Use Specification to find matching students
	        List<Student> studentList = studentRepository.findAll(StudentSpecification.getStudentSpec(studentGetRequest));

	        if (studentList.isEmpty()) {
	            log.info("No students found for the given filter criteria.");
	            return Collections.emptyList();
	        }

	        // Map entities to response DTOs
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

	    } catch (Exception e) {
	        log.error("Error occurred while fetching students: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while fetching students", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public StudentResponse saveStudent(StudentCreateRequest studentRequest) {
	    log.info("Starting to save student with request: {}", studentRequest);

	    // Validate the request
	    validateStudentRequest(studentRequest);

	    log.debug("Mapping StudentRequest to Student entity.");
	    Student newStudent = modelMapper.map(studentRequest, Student.class);

	    // Fetch department dynamically
	    log.info("Fetching department with ID: {}", studentRequest.getDepartmentId());
	    Department department = departmentRepository.findById(studentRequest.getDepartmentId())
	            .orElseThrow(() -> new CustomException(
	                    ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorMessage(),
	                    HttpStatus.NOT_FOUND
	            ));

	    // Fetch faculty dynamically
	    log.info("Fetching faculty with ID: {}", studentRequest.getMentorId());
	    Faculty faculty = facultyRepository.findById(studentRequest.getMentorId())
	            .orElseThrow(() -> new RuntimeException("Faculty not found for ID: " + studentRequest.getMentorId()));

	    // Set department and faculty in the student entity
	    log.debug("Setting department and faculty for the student entity.");
	    newStudent.setDepartment(department);
	    newStudent.setFaculty(faculty);

	    try {
	        // Save the student entity and handle any persistence-related issues
	        log.info("Saving the student entity with student ID: {}", newStudent.getStudentId());
	        Student savedStudent = studentRepository.save(newStudent);

	        // Map the saved student to response DTO
	        log.debug("Mapping saved student entity to StudentResponse.");
	        StudentResponse studentResponse = modelMapper.map(savedStudent, StudentResponse.class);
	        studentResponse.setDepartmentId(savedStudent.getDepartment().getDepartmentId());
	        studentResponse.setMentorId(savedStudent.getFaculty().getFacultyId());

	        log.info("Student saved successfully with ID: {}", savedStudent.getStudentId());
	        return studentResponse;

	    } catch (Exception e) {
	        log.error("Unexpected error occurred while saving student: {}", e.getMessage());
	        throw new CustomException("Failed to save student", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	private void validateStudentRequest(StudentCreateRequest studentRequest) {
		
		if (validatorRuleStatus.isRuleActive(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
	        log.info("Validation rule '{}' is active, starting validation for student request.", ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
	        Validator<StudentCreateRequest> validator = (Validator<StudentCreateRequest>) applicationContext.getBean(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getValidatorClass());
	        validator.validate(studentRequest);
	       
	    }
		 
	   
	}

	@Override
	public String saveMultipleStudents(List<StudentCreateRequest> studentRequests) {
	    log.info("Starting to save multiple students, total records: {}", studentRequests.size());

	    try {
	        // Validate each student request if validation rule is active
	        if (validatorRuleStatus.isRuleActive(ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("Validation rule '{}' is active, starting validation for multiple student requests.", ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
	            Validator<StudentCreateRequest> validator = applicationContext.getBean(StudentRequestValidator.class);

	            studentRequests.forEach(request -> {
	                log.debug("Validating student request for student ID: {}", request.getStudentId());
	                validator.validate(request);
	            });
	            log.info("Completed validation for all student requests.");
	        } 
	        else {
	            log.warn("Validation rule '{}' is not active, skipping validation for student requests.", ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
	        }

	        // Map each student request to Student entity and fetch related data
	        log.debug("Converting StudentRequest list to Student entities...");
	        List<Student> students = studentRequests.stream()
	            .map(request -> {
	                log.debug("Mapping StudentRequest to Student entity for student ID: {}", request.getStudentId());
	                Student student = modelMapper.map(request, Student.class);

	                // Fetch department
	                Department department = departmentRepository.findById(request.getDepartmentId())
	                        .orElseThrow(() -> new CustomException(
	                                ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorMessage(),
	                                HttpStatus.NOT_FOUND
	                        ));

	                // Fetch faculty
	                Faculty faculty = facultyRepository.findById(request.getMentorId())
	                        .orElseThrow(() -> new RuntimeException("Faculty not found for ID: " + request.getMentorId()));

	                // Set department and faculty in the student entity
	                student.setDepartment(department);
	                student.setFaculty(faculty);
	                return student;
	            })
	            .collect(Collectors.toList());

	        // Save all student entities in one batch
	        log.info("Saving all student records...");
	        List<Student> savedStudents = studentRepository.saveAll(students);

	        // Log the result of saving student records
	        if (savedStudents.size() == studentRequests.size()) {
	            log.info("Successfully saved {} students.", savedStudents.size());
	            return "Successfully saved " + savedStudents.size() + " students";
	        } else {
	            log.warn("Mismatch in records saved: expected {} but got {}", studentRequests.size(), savedStudents.size());
	            return "Mismatch in saved records";
	        }

	    } catch (Exception e) {
	        log.error("Unexpected error occurred while saving multiple students: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while saving students", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	
	
	
	private List<String> validateStudent(Student  student) {
//		List<String> errors = validateStudent(newStudent);    
	//	
//		if(!errors.isEmpty()) {
//			log.info("Errors exist in studen`t data {}",errors);
//			return null;
//		}
    	List<String> errors = new ArrayList<>();
        log.info("Starting validation for Attendance Data.");
        
        String[] rules = studentValidationRules.split(",");

        for (String rule : rules) {
            try {
                ValidatorRuleEnum validatorRule = ValidatorRuleEnum.getValidatorRuleByRuleName(rule.trim());
                Validator<?> validator = (Validator<?>) applicationContext.getBean(validatorRule.getValidatorClass());

                Boolean isValid = true;
                
//                if (validatorRule == ValidatorRuleEnum.STUDENT_VALIDATOR) {
//                    isValid = ((Validator<Student>) validator).validateAndGetResult(student);
//                } else if (validatorRule == ValidatorRuleEnum.DEPARTMENT_VALIDATOR) {
//                    isValid = ((Validator<Department>) validator).validateAndGetResult(student.getDepartment());
//                }
               
                if (!isValid) {
                    errors.add("Validation failed for rule: " + rule);
                    log.warn("Validation failed for rule: {}", rule);
                } else {
                    log.info("Validation passed for rule: {}", rule);
                }
            } catch (Exception e) {
                errors.add("Error processing rule: " + rule + " - " + e.getMessage());
                log.error("Error processing rule: {} - {}", rule, e.getMessage());
            }
        }

        if (!errors.isEmpty()) { // Non-empty errors list means errors exist
            log.error("Validation Errors: {}", errors);
            return errors;
        }
            
        log.info("All validations passed successfully.");
     
        return errors;
    }

	
		
		

}


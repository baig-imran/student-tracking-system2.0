package com.sts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.StudentRequest;
import com.sts.dto.StudentResponse;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Student;
import com.sts.exceptions.DepartmentIdValidationException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.StudentRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.StudentService;
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
	private final ValidatorRuleStatus validatorRuleService;

	public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleService) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.applicationContext = applicationContext;
		this.validatorRuleService = validatorRuleService;
	}


	@Override
	public StudentResponse saveStudent(StudentRequest studentRequest){

	    log.info("Starting to save student with request: {}", studentRequest);

	    // Validate the request
	    if (validatorRuleService.isRuleActive(ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
	        log.info("Starting {}", ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
	        Validator<StudentRequest> validator = applicationContext.getBean(StudentRequestValidator.class);
	        validator.validate(studentRequest);
	    }

	    log.debug("Mapping StudentRequest to Student entity.");
	    Student newStudent = modelMapper.map(studentRequest, Student.class);

	        // Fetch department dynamically
	        log.info("Fetching department with ID: {}", studentRequest.getDepartmentId());
	        Department department = departmentRepository.findById(studentRequest.getDepartmentId())
	                .orElseThrow(() -> {
	                    log.error("Department not found for ID: {}", studentRequest.getDepartmentId());
	                    return new DepartmentIdValidationException(
	                            ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorCode(),
	                            ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorMessage(),
	                            HttpStatus.NOT_FOUND
	                    );
	                });

	        // Fetch faculty dynamically
	        log.info("Fetching faculty with ID: {}", studentRequest.getMentorId());
	        Faculty faculty = facultyRepository.findById(studentRequest.getMentorId())
	                .orElseThrow(() -> {
	                    log.error("Faculty not found for ID: {}", studentRequest.getMentorId());
	                    return new RuntimeException("Faculty not found for ID: " + studentRequest.getMentorId());
	                });

	        // Set department and faculty in the student entity
	        log.debug("Setting department and faculty for the student entity.");
	        newStudent.setDepartment(department);
	        newStudent.setFaculty(faculty);

	        try {
	        // Save the student entity
	        log.info("Saving the student entity with student ID: {}", newStudent.getStudentId());
	        Student savedStudent = studentRepository.save(newStudent);

	        // Map the saved student to response
	        log.debug("Mapping saved student entity to StudentResponse.");
	        StudentResponse studentResponse = modelMapper.map(savedStudent, StudentResponse.class);
	        studentResponse.setDepartmentId(savedStudent.getDepartment().getDepartmentId());
	        studentResponse.setMentorId(savedStudent.getFaculty().getFacultyId());

	        log.info("Student saved successfully with ID: {}", savedStudent.getStudentId());
	        return studentResponse;

	    } catch (Exception e) {
	        log.error("Unexpected error saving student: {}", e.getMessage());
	        throw e;
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
                
                if (validatorRule == ValidatorRuleEnum.STUDENT_VALIDATOR) {
                    isValid = ((Validator<Student>) validator).validateAndGetResult(student);
                } else if (validatorRule == ValidatorRuleEnum.DEPARTMENT_VALIDATOR) {
                    isValid = ((Validator<Department>) validator).validateAndGetResult(student.getDepartment());
                }
               
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


package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;
import com.sts.entity.Exam;
import com.sts.exceptions.CustomException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.ExamRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.ExamRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.ExamService;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final SemesterRepository semesterRepository;
	private final ValidatorRuleStatus validatorRuleService;
	private final ApplicationContext applicationContext;
	private final ExamRepository examRepository;


	public ExamServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, SemesterRepository semesterRepository, ValidatorRuleStatus validatorRuleService, ApplicationContext applicationContext, ExamRepository examRepository) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.semesterRepository = semesterRepository;
		this.validatorRuleService = validatorRuleService;
		this.applicationContext = applicationContext;
		this.examRepository = examRepository;
	}

	@Override
	public ExamResponse saveExam(ExamRequest examRequest) {
	    log.info("Starting to save exam with request: {}", examRequest);

	    try {
	        // Validate the request if the validation rule is active
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("Validation rule '{}' is active, starting validation...", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
	            Validator<ExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);
	            validator.validate(examRequest);  // Validate each exam request
	            log.info("Validation successful for request: {}", examRequest);
	        } else {
	            log.warn("Validation rule '{}' is not active, skipping validation.", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
	        }

	        // Map the exam request to exam entity
	        log.debug("Mapping ExamRequest to Exam entity.");
	        Exam newExam = modelMapper.map(examRequest, Exam.class);

	        // Dynamically set semester and student based on the request
	        log.info("Fetching semester with code: {}", examRequest.getSemesterCode());
	        newExam.setSemester(semesterRepository.getSemesterBySemesterCode(examRequest.getSemesterCode()));

	        log.info("Fetching student with ID: {}", examRequest.getStudentId());
	        newExam.setStudent(studentRepository.getStudentByStudentId(examRequest.getStudentId()));

	        // Save the exam entity to the repository
	        log.info("Saving the exam entity: {}", newExam);
	        Exam savedExam = examRepository.save(newExam);
	        log.info("Exam saved successfully with ID: {}", savedExam.getExamCode());

	        // Map the saved exam to the response DTO
	        ExamResponse examResponse = modelMapper.map(savedExam, ExamResponse.class);
	        examResponse.setSemesterCode(savedExam.getSemester().getSemesterCode());
	        examResponse.setStudentId(savedExam.getStudent().getStudentId());

	        log.info("Returning saved exam response with ID: {}", examResponse.getExamCode());
	        return examResponse;  // Return the saved exam response

	    } catch (Exception e) {
	        // Log the exception message and rethrow as a CustomException
	        log.error("Error saving exam: {}", e.getMessage());
	        throw new CustomException("Failed to save exam", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@Override
	public String updateExam(ExamUpdateRequest examUpdateRequest) {
	    log.info("Starting to update exam for student ID: {} and subject: {}", 
	             examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode());

	    // Map the exam update request to the Exam entity
	    Exam updatedExam = modelMapper.map(examUpdateRequest, Exam.class);

	    int response;

	    try {
	        // Call the repository method to update the exam status
	        response = examRepository.updateExam(
	                examUpdateRequest.getStudentId(),
	                examUpdateRequest.getSubjectCode(),
	                examUpdateRequest.getExamName(),
	                examUpdateRequest.getMarksObtained()
	        );

	        // Check if the update was successful
	        if (response > 0) {
	            log.info("Successfully updated exam for student ID: {} and subject: {}", 
	                     examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode());
	            
	            return "Successfully updated exam for student ID: " + examUpdateRequest.getStudentId()
	                    + " and subject: " + examUpdateRequest.getSubjectCode();
	        } else {
	            // If no records are updated, throw an exception
	            String errorMessage = "No records found to update for student ID: " 
	                                  + examUpdateRequest.getStudentId() 
	                                  + " and subject: " + examUpdateRequest.getSubjectCode();
	            log.error(errorMessage);
	            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);  // Throw CustomException with BAD_REQUEST status
	        }

	    } catch (Exception e) {
	        log.error("Unexpected error occurred while updating exam for student ID: {} and subject: {}: {}", 
	                  examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode(), e.getMessage());
	        throw new CustomException("Unexpected error occurred while updating exam", HttpStatus.INTERNAL_SERVER_ERROR); // Throw CustomException with INTERNAL_SERVER_ERROR status
	    }
	}














}

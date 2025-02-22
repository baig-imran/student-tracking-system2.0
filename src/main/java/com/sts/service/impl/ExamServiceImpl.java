package com.sts.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import com.sts.specification.ExamSpecification;
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
	public List<ExamResponse> getExams(ExamRequest filterRequest) {
	    log.info("Fetching exam records based on filter criteria: {}", filterRequest);

	    try {
	        // Use Specification to find matching exam records
	        List<Exam> examList = examRepository.findAll(ExamSpecification.getExamSpec(filterRequest));

	        if (examList.isEmpty()) {
	            log.info("No exam records found for the given filter criteria.");
	            return Collections.emptyList();
	        }

	        // Map entities to response DTOs
	        List<ExamResponse> responseList = examList.stream()
	                .map(exam -> {
	                    ExamResponse response = modelMapper.map(exam, ExamResponse.class);
	                    response.setStudentId(exam.getStudent().getStudentId());
	                    response.setSemesterCode(exam.getSemester().getSemesterCode());
	                    return response;
	                })
	                .collect(Collectors.toList());

	        log.info("Successfully fetched {} exam records.", responseList.size());
	        return responseList;

	    } catch (Exception e) {
	        log.error("Error occurred while fetching exam records: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while fetching exam records", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
	        log.error("Error saving exam: {}", e.getMessage());
	        throw new CustomException("Failed to save exam", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public String updateExam(ExamUpdateRequest examUpdateRequest) {
	    log.info("Starting to update exam for student ID: {} and subject: {}", 
	             examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode());

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

	@Override
	public String saveMultipleExams(List<ExamRequest> examRequests) {
	    log.info("Starting to save multiple exam records, total records: {}", examRequests.size());

	    try {
	        // Check if exam request validation rule is active
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("Validation rule '{}' is active, starting validation...", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
	            Validator<ExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);

	            // Validate each exam request
	            examRequests.forEach(request -> {
	                log.debug("Validating exam request for student ID: {}", request.getStudentId());
	                validator.validate(request);
	            });
	            log.info("Validation completed for all exam requests.");
	        } else {
	            log.warn("Validation rule '{}' is not active, skipping validation.", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
	        }

	        // Log the conversion of ExamRequest to Exam entities
	        log.debug("Converting ExamRequest list to Exam entities...");
	        List<Exam> examEntities = examRequests.stream()
	            .map(request -> {
	                log.debug("Mapping ExamRequest for student ID: {} to Exam entity", request.getStudentId());
	                Exam exam = modelMapper.map(request, Exam.class);
	                exam.setStudent(studentRepository.getStudentByStudentId(request.getStudentId()));
	                exam.setSemester(semesterRepository.getSemesterBySemesterCode(request.getSemesterCode()));
	                return exam;
	            })
	            .collect(Collectors.toList());

	        // Log before saving the exam records
	        log.info("Saving all exam records in a single batch...");
	        List<Exam> savedExams = examRepository.saveAll(examEntities);

	        // Log the result of saving exam records
	        if (savedExams.size() == examRequests.size()) {
	            log.info("Successfully saved {} exam records", savedExams.size());
	            return "Successfully saved " + savedExams.size() + " exam records";
	        } else {
	            String errorMessage = "Mismatch in records saved: expected " + examRequests.size() + " but got " + savedExams.size();
	            log.error(errorMessage);
	            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);  // Throw custom exception with BAD_REQUEST status
	        }
	    } catch (CustomException e) {
	        // Log the custom exception and rethrow
	        log.error("Error occurred while saving exam records: {}", e.getMessage());
	        throw e;  // Rethrow the custom exception
	    } catch (Exception e) {
	        // Log unexpected errors and throw a custom exception with INTERNAL_SERVER_ERROR status
	        log.error("Unexpected error occurred while saving exam records: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while saving exam records", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public String updateMultipleExams(List<ExamUpdateRequest> examUpdateRequests) {
	    log.info("Starting to update multiple exam records, total records: {}", examUpdateRequests.size());

	    int totalUpdated = 0;

	    try {
	        for (ExamUpdateRequest request : examUpdateRequests) {
	            log.debug("Updating exam record for student ID: {} and subject: {}", request.getStudentId(), request.getSubjectCode());

	            // Call repository method to update exam
	            int updated = examRepository.updateExam(
	                request.getStudentId(),
	                request.getSubjectCode(),
	                request.getExamName(),
	                request.getMarksObtained()
	            );

	            if (updated > 0) {
	                log.info("Successfully updated exam for student ID: {} and subject: {}", request.getStudentId(), request.getSubjectCode());
	                totalUpdated++;
	            } else {
	                log.warn("No records found to update for student ID: {} and subject: {}", request.getStudentId(), request.getSubjectCode());
	            }
	        }

	        // Check if all records were updated successfully
	        if (totalUpdated == examUpdateRequests.size()) {
	            log.info("Successfully updated {} exam records", totalUpdated);
	            return "Successfully updated " + totalUpdated + " exam records";
	        } else {
	            String errorMessage = "Mismatch in records updated: expected " + examUpdateRequests.size() + " but updated " + totalUpdated;
	            log.error(errorMessage);
	            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
	        }

	    } catch (Exception e) {
	        log.error("Unexpected error occurred while updating multiple exam records: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while updating multiple exams", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}





}

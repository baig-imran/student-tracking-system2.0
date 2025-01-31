package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;
import com.sts.entity.Exam;
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
				log.info("Starting validation using rule: {}", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
				Validator<ExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);
				validator.validate(examRequest);
				log.info("Validation successful for request: {}", examRequest);
			} else {
				log.warn("Validation rule '{}' is not active, skipping validation.", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
			}

			// Map the exam request to exam entity
			log.debug("Mapping ExamRequest to Exam entity.");
			Exam newExam = modelMapper.map(examRequest, Exam.class);

			// Set semester and student dynamically
			log.info("Fetching semester with code: {}", examRequest.getSemesterCode());
			newExam.setSemester(semesterRepository.getSemesterBySemesterCode(examRequest.getSemesterCode()));

			log.info("Fetching student with ID: {}", examRequest.getStudentId());
			newExam.setStudent(studentRepository.getStudentByStudentId(examRequest.getStudentId()));

			// Save the exam
			log.info("Saving the exam entity: {}", newExam);
			Exam savedExam = examRepository.save(newExam);
			log.info("Exam saved successfully with ID: {}", savedExam.getExamCode());

			// Map saved exam to response DTO
			ExamResponse examResponse = modelMapper.map(savedExam, ExamResponse.class);
			examResponse.setSemesterCode(savedExam.getSemester().getSemesterCode());
			examResponse.setStudentId(savedExam.getStudent().getStudentId());

			log.info("Returning saved exam response with ID: {}", examResponse.getExamCode());
			return examResponse;

		} catch (Exception e) {
			log.error("Error saving exam: {}", e.getMessage());
			throw new RuntimeException("Failed to save exam");
		}
	}

	@Override
	public boolean updateExam(ExamUpdateRequest examUpdateRequest) {

		log.info("Starting to update exam with request: {}", examUpdateRequest);


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
				log.info("Exam updated successfully");
				return true;
			} else {
				log.warn("No records found to update");
				return false;
			}
		} catch (Exception e) {
			log.error("Error occurred while updating exam: ", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}











}

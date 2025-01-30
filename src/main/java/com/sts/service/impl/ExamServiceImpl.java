package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
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

	    // Validate the request
	    if (validatorRuleService.isRuleActive(ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName())) {
	        log.info("Starting {}", ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName());
	        Validator<ExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);
	        validator.validate(examRequest);
	    }
	    
	    Exam newExam = modelMapper.map(examRequest, Exam.class);
	    newExam.setSemester(semesterRepository.getSemesterBySemesterCode(examRequest.getSemesterCode()));
	    newExam.setStudent(studentRepository.getStudentByStudentId(examRequest.getStudentId()));
	    Exam savedExam = examRepository.save(newExam);
	    
	    ExamResponse examResponse = modelMapper.map(savedExam, ExamResponse.class);
	    
	    examResponse.setSemesterCode(savedExam.getSemester().getSemesterCode());
	    examResponse.setStudentId(savedExam.getStudent().getStudentId());
	    
	    
	    
		return examResponse;
	}

	

	

	

	
}

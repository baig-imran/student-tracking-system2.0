package com.sts.controller;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.SemesterSubject;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterFacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.SemesterStudentRepository;
import com.sts.repository.SemesterSubjectRepository;
import com.sts.repository.StudentRepository;
import com.sts.repository.StudentSubjectRepository;
import com.sts.repository.SubjectRepository;
import com.sts.service.impl.validators.ValidatorRuleStatus;


@RestController
@RequestMapping
public class TestController {
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final ApplicationContext applicationContext;
	private final ValidatorRuleStatus validatorRuleService;
	private final SemesterRepository semesterRepository;
	private final SubjectRepository subjectRepository;
	private final SemesterFacultyRepository semesterFacultyRepository;
	private final SemesterStudentRepository semesterStudentRepository; 
	private final SemesterSubjectRepository semesterSubjectRepository;
	private final StudentSubjectRepository studentSubjectRepository;

	public TestController(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleService, SemesterRepository semesterRepository, SubjectRepository subjectRepository, SemesterFacultyRepository semesterFacultyRepository, SemesterStudentRepository semesterStudentRepository, SemesterSubjectRepository semesterSubjectRepository, StudentSubjectRepository studentSubjectRepository) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.applicationContext = applicationContext;
		this.validatorRuleService = validatorRuleService;
		this.semesterRepository = semesterRepository;
		this.subjectRepository = subjectRepository;
		this.semesterFacultyRepository = semesterFacultyRepository;
		this.semesterStudentRepository = semesterStudentRepository;
		this.semesterSubjectRepository = semesterSubjectRepository;
		this.studentSubjectRepository = studentSubjectRepository;
	}
	
	@GetMapping("/test1")
	public ResponseEntity<?> test() {
		
		return ResponseEntity.ok(
				"test"
				);
	}

}


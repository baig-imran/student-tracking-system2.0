package com.sts.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.SemesterRequest;
import com.sts.dto.SemesterResponse;
import com.sts.dto.StudentRequest;
import com.sts.dto.StudentResponse;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Semester;
import com.sts.entity.Student;
import com.sts.exceptions.DepartmentIdValidationException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.StudentRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.SemesterService;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SemesterServiceImpl implements SemesterService {
	
	private String studentValidationRules = "STUDENT_VALIDATOR,FACULTY_VALIDATOR,DEPARTMENT_VALIDATOR,EXAM_VALIDATOR,ATTENDANCE_VALIDATOR,SUBJECT_VALIDATOR,SUBJECT_SEMESTER_VALIDATOR,SEMESTER_VALIDATOR";
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final ApplicationContext applicationContext;
	private final ValidatorRuleStatus validatorRuleService;
	private final SemesterRepository semesterRepository;

	public SemesterServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleService, SemesterRepository semesterRepository) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.applicationContext = applicationContext;
		this.validatorRuleService = validatorRuleService;
		this.semesterRepository = semesterRepository;
	}


	
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

	
	
	
	@Override
	public Semester getSemester(String semesterCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	 public SemesterResponse saveSemester(SemesterRequest semesterRequest) {
        // Fetch the department entity using departmentId
        Department department = departmentRepository.findById(semesterRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Create a new Semester entity
        Semester newSemester = new Semester();
        newSemester.setSemesterCode(semesterRequest.getSemesterCode());
        newSemester.setSemesterNumber(semesterRequest.getSemesterNumber());
        newSemester.setStudyYear(semesterRequest.getStudyYear());
        newSemester.setAcademicYear(semesterRequest.getAcademicYear());
        newSemester.setRegulation(semesterRequest.getRegulation());
        newSemester.setStartDate(semesterRequest.getStartDate());
        newSemester.setEndDate(semesterRequest.getEndDate());
        newSemester.setBatch(semesterRequest.getBatch());
        newSemester.setDepartmentId(department.getDepartmentId()); // Set the department

        // Fetch and set students for the semester
        List<Student> students = studentRepository.findAllByStudentIdIn(semesterRequest.getStudents());
        newSemester.setStudents(students);

        // Fetch and set faculties for the semester
        List<Faculty> faculties = facultyRepository.findAllByFacultyIdIn(semesterRequest.getFaculties());
        newSemester.setFaculties(faculties);

        // Save the semester
        log.info("Saving semester data into DB");
        Semester savedSemester = semesterRepository.save(newSemester);
        
        log.info(savedSemester.toString());
        

        // Return response (you can map it to a response DTO if necessary)
        return null;//new SemesterResponse(savedSemester);
    }
	
		
		

}


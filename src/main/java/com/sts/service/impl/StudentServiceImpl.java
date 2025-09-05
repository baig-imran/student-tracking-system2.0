package com.sts.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.dto.student.DeleteStudentsByIdsReq;
import com.sts.dto.student.GetStudentsByIdsReq;
import com.sts.dto.student.StudentCreateRequest;
import com.sts.dto.student.StudentGetRequest;
import com.sts.dto.student.StudentResponse;
import com.sts.dto.student.StudentUpdateRequest;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Student;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.ObjectValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.StudentService;
import com.sts.specification.StudentSpecification;
import com.sts.validator.Validator;

import jakarta.transaction.Transactional;
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
	public StudentResponse getStudentById(String studentId) {
	    log.info("Fetching student with ID: {}", studentId);

	    Student student = studentRepository.findById(studentId)
	        .orElseThrow(() -> new ResourceNotFoundException(
	            ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(studentId)));

	    StudentResponse response = mapToStudentResponse(student);

	    log.info("Successfully fetched student with ID: {}", studentId);
	    return response;
	}
	
	private StudentResponse mapToStudentResponse(Student student) {
	    StudentResponse response = modelMapper.map(student, StudentResponse.class);

	    if (student.getFaculty() != null) {
	        response.setMentorId(student.getFaculty().getFacultyId());
	    }

	    if (student.getDepartment() != null) {
	        response.setDepartmentId(student.getDepartment().getDepartmentId());
	    }

	    return response;
	}


	
	

	@Override
	public List<StudentResponse> getStudentsByCriteria(StudentGetRequest studentGetRequest) {
	    log.info("Fetching students based on filter criteria: {}", studentGetRequest);

	    ObjectValidator.isObjectEmpty(studentGetRequest);

	    List<Student> students = studentRepository.findAll(StudentSpecification.getStudentSpec(studentGetRequest));

	    if (students.isEmpty()) {
	        log.info("No students found for the given filter criteria.");
	       throw new ResourceNotFoundException(ErrorMessageEnum.STUDENTS_NOT_FOUND.getMessage());
	    }

	    List<StudentResponse> responses = students.stream()
	            .map(this::mapToStudentResponse)
	            .collect(Collectors.toList());

	    log.info("Successfully fetched {} students.", responses.size());
	    return responses;
	}
	
	


	@Override
	@Transactional
	public StudentResponse saveStudent(StudentCreateRequest studentRequest) {
		log.info("Starting to save student with request: {}", studentRequest);
		validateStudentRequest(studentRequest);

		Student newStudent = modelMapper.map(studentRequest, Student.class);

		log.info("Fetching department with ID: {}", studentRequest.getDepartmentId());
		Department department = departmentRepository.findById(studentRequest.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(studentRequest.getDepartmentId()))
						);

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
	@Transactional
	public String saveMultipleStudents(List<StudentCreateRequest> studentRequests) {
	    log.info("Starting to save multiple students, total records: {}", studentRequests.size());

	    studentRequests.forEach(this::validateStudentRequest);

	    // Prefetch related entities
	    Set<String> departmentIds = studentRequests.stream()
	            .map(StudentCreateRequest::getDepartmentId)
	            .collect(Collectors.toSet());

	    Set<String> mentorIds = studentRequests.stream()
	            .map(StudentCreateRequest::getMentorId)
	            .collect(Collectors.toSet());

	    Map<String, Department> departmentMap = departmentRepository.findAllById(departmentIds).stream()
	            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

	    Map<String, Faculty> facultyMap = facultyRepository.findAllById(mentorIds).stream()
	            .collect(Collectors.toMap(Faculty::getFacultyId, Function.identity()));

	    List<Student> students = studentRequests.stream()
	            .map(request -> {
	                Student student = modelMapper.map(request, Student.class);

	                Department department = departmentMap.get(request.getDepartmentId());
	                if (department == null) {
	                    throw new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(request.getDepartmentId()));
	                }

	                Faculty faculty = facultyMap.get(request.getMentorId());
	                if (faculty == null) {
	                    throw new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(request.getMentorId()));
	                }

	                student.setDepartment(department);
	                student.setFaculty(faculty);
	                return student;
	            })
	            .collect(Collectors.toList());

	    studentRepository.saveAll(students);

	    log.info("Successfully saved {} students.", students.size());
	    return "Successfully saved " + students.size() + " students";
	}


	@Override
	public StudentResponse updateStudent(StudentUpdateRequest studentRequest) {
	    log.info("Starting to update student with request: {}", studentRequest);

	    // Fetch existing student
	    Student existingStudent = studentRepository.findById(studentRequest.getStudentId())
	            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(studentRequest.getStudentId())));

	    // Update basic fields (except relations)
	    modelMapper.map(studentRequest, existingStudent); // Assuming studentId is ignored in mapping config

	    // Fetch and assign department
	    Department department = departmentRepository.findById(studentRequest.getDepartmentId())
	            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(studentRequest.getDepartmentId())));
	    existingStudent.setDepartment(department);

	    // Fetch and assign faculty
	    Faculty faculty = facultyRepository.findById(studentRequest.getMentorId())
	            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(studentRequest.getMentorId())));
	    existingStudent.setFaculty(faculty);

	    // Save the updated student
	    Student savedStudent = studentRepository.save(existingStudent);

	    // Prepare response
	    StudentResponse studentResponse = modelMapper.map(savedStudent, StudentResponse.class);
	    studentResponse.setDepartmentId(savedStudent.getDepartment().getDepartmentId());
	    studentResponse.setMentorId(savedStudent.getFaculty().getFacultyId());

	    log.info("Student updated successfully with ID: {}", savedStudent.getStudentId());
	    return studentResponse;
	}


	private void validateStudentRequest(StudentCreateRequest studentRequest) {

		if (validatorRuleStatus.isRuleActive(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
			log.info("Validation rule '{}' is active, starting validation for student request.", ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
			Validator<StudentCreateRequest> validator = (Validator<StudentCreateRequest>) applicationContext.getBean(ValidatorRuleEnum.STUDENT_REQUEST_VALIDATOR.getValidatorClass());
			validator.validate(studentRequest);
		}
	}



	
	
	@Override
	public List<StudentGetRequest> getStudentsByStudentIds(GetStudentsByIdsReq req) {
	    log.info("Fetching students for IDs: {}", req.getStudentIds());

	    List<String> requestedIds = req.getStudentIds();
	    List<Student> students = studentRepository.findAllById(requestedIds);

	    if (students.isEmpty()) {
	        log.info("No students found for given IDs.");
	        return Collections.emptyList();
	    }

	    // Validate that all requested IDs exist
	    Set<String> foundIds = students.stream()
	            .map(Student::getStudentId)
	            .collect(Collectors.toSet());

	    List<String> notFoundIds = requestedIds.stream()
	            .filter(id -> !foundIds.contains(id))
	            .collect(Collectors.toList());

	    if (!notFoundIds.isEmpty()) {
	        throw new ResourceNotFoundException(
	            ErrorMessageEnum.STUDENT_IDS_NOT_FOUND.getMessage(notFoundIds.toString())
	        );
	    }

	    return students.stream()
	            .map(student -> {
	                StudentGetRequest dto = modelMapper.map(student, StudentGetRequest.class);
	                dto.setDepartmentId(student.getDepartment().getDepartmentId());
	                dto.setMentorId(student.getFaculty().getFacultyId());
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String updateBulkStudents(List<StudentUpdateRequest> reqs) {
	    log.info("Starting bulk update for {} students.", reqs.size());

	    // 1. Collect all needed IDs
	    Set<String> studentIds = reqs.stream()
	            .map(StudentUpdateRequest::getStudentId)
	            .collect(Collectors.toSet());

	    Set<String> departmentIds = reqs.stream()
	            .map(StudentUpdateRequest::getDepartmentId)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());

	    Set<String> facultyIds = reqs.stream()
	            .map(StudentUpdateRequest::getMentorId)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());

	    // 2. Bulk fetch all entities
	    Map<String, Student> studentsMap = studentRepository.findAllById(studentIds).stream()
	            .collect(Collectors.toMap(Student::getStudentId, Function.identity()));

	    Map<String, Department> departmentsMap = departmentRepository.findAllById(departmentIds).stream()
	            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

	    Map<String, Faculty> facultiesMap = facultyRepository.findAllById(facultyIds).stream()
	            .collect(Collectors.toMap(Faculty::getFacultyId, Function.identity()));

	    List<Student> updatedStudents = new ArrayList<>();

	    for (StudentUpdateRequest req : reqs) {
	        Student existing = studentsMap.get(req.getStudentId());
	        if (existing == null) {
	            throw new ResourceNotFoundException(
	                    ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(req.getStudentId()));
	        }

	        // Partial update
	        if (req.getStudentName() != null) existing.setStudentName(req.getStudentName());
	        if (req.getStudentMobileNumber() != null) existing.setStudentMobileNumber(req.getStudentMobileNumber());
	        if (req.getFatherName() != null) existing.setFatherName(req.getFatherName());
	        if (req.getFatherMobileNumber() != null) existing.setFatherMobileNumber(req.getFatherMobileNumber());
	        if (req.getIsGraduated() != null) existing.setIsGraduated(req.getIsGraduated());
	        if (req.getBatch() != null) existing.setBatch(req.getBatch());
	        if (req.getRegulation() != null) existing.setRegulation(req.getRegulation());

	        if (req.getDepartmentId() != null) {
	            Department dept = departmentsMap.get(req.getDepartmentId());
	            if (dept == null) {
	                throw new ResourceNotFoundException(
	                        ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(req.getDepartmentId()));
	            }
	            existing.setDepartment(dept);
	        }

	        if (req.getMentorId() != null) {
	            Faculty mentor = facultiesMap.get(req.getMentorId());
	            if (mentor == null) {
	                throw new ResourceNotFoundException(
	                        ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(req.getMentorId()));
	            }
	            existing.setFaculty(mentor);
	        }

	        updatedStudents.add(existing);
	    }

	    studentRepository.saveAll(updatedStudents);
	    log.info("Successfully updated {} students.", updatedStudents.size());
	    return "Successfully updated " + updatedStudents.size() + " students";
	}

	
	@Override
	public String deleteById(String studentId) {
	    log.info("Attempting to delete student with ID: {}", studentId);

	    if (!studentRepository.existsById(studentId)) {
	        log.warn("Student ID {} not found for deletion", studentId);
	        throw new ResourceNotFoundException(
	            ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(studentId)
	        );
	    }

	    studentRepository.deleteById(studentId);

	    log.info("Student with ID {} deleted successfully", studentId);
	    return "Student with ID " + studentId + " deleted successfully";
	}


	@Override
	@Transactional
	public List<String> deleteByIds(List<String>  req) {
	    List<String> idsToDelete = req;
	    log.info("Attempting bulk deletion for {} student IDs", idsToDelete.size());

	    // 1. Fetch existing students only
	    List<Student> students = studentRepository.findAllById(idsToDelete);
	    Set<String> existingIds = students.stream()
	            .map(Student::getStudentId)
	            .collect(Collectors.toSet());

	    // 2. Determine missing IDs
	    List<String> missingIds = idsToDelete.stream()
	            .filter(id -> !existingIds.contains(id))
	            .collect(Collectors.toList());

	    if (!missingIds.isEmpty()) {
	        log.warn("Some student IDs were not found: {}", missingIds);
	        throw new ResourceNotFoundException(
	            ErrorMessageEnum.STUDENT_IDS_NOT_FOUND.getMessage(String.join(", ", missingIds))
	        );
	    }

	    try {
	        studentRepository.deleteAllById(existingIds);
	        studentRepository.flush();  // Forces immediate execution of SQL
	        log.info("Successfully deleted {} students", existingIds.size());
	    } catch (Exception e) {
	        log.error("Failed to delete students with IDs {}: {}", existingIds, e.getMessage());
	        throw e;
	    }



	    // 4. Return confirmation messages
	    return existingIds.stream()
	            .map(id -> "Student with ID " + id + " deleted successfully")
	            .collect(Collectors.toList());
	}





}



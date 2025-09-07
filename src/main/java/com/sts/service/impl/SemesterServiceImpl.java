//package com.sts.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;
//
//import com.sts.constants.ErrorMessageEnum;
//import com.sts.constants.ValidatorRulesEnum;
//import com.sts.dto.semester.SemBasicDetailsRes;
//import com.sts.dto.semester.SemesterDetails;
//import com.sts.dto.semester.SemesterResponse;
//import com.sts.dto.student.StudentCreateRequest;
//import com.sts.dto.student.StudentResponse;
//import com.sts.entity.Department;
//import com.sts.entity.Faculty;
//import com.sts.entity.Semester;
//import com.sts.entity.Student;
//import com.sts.exceptions.ResourceNotFoundException;
//import com.sts.repository.DepartmentRepository;
//import com.sts.repository.FacultyRepository;
//import com.sts.repository.SemesterRepository;
//import com.sts.repository.StudentRepository;
//import com.sts.service.impl.validators.StudentRequestValidator;
//import com.sts.service.impl.validators.ValidatorRuleStatus;
//import com.sts.service.interfaces.SemesterService;
//import com.sts.validator.Validator;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class SemesterServiceImpl implements SemesterService {
//	
//	private final StudentRepository studentRepository;
//	private final ModelMapper modelMapper;
//	private final FacultyRepository facultyRepository;
//	private final DepartmentRepository departmentRepository;
//	private final ApplicationContext applicationContext;
//	private final ValidatorRuleStatus validatorRuleService;
//	private final SemesterRepository semesterRepository;
//
//	public SemesterServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleService, SemesterRepository semesterRepository) {
//		this.studentRepository = studentRepository;
//		this.modelMapper = modelMapper;
//		this.facultyRepository = facultyRepository;
//		this.departmentRepository = departmentRepository;
//		this.applicationContext = applicationContext;
//		this.validatorRuleService = validatorRuleService;
//		this.semesterRepository = semesterRepository;
//	}
//
//
//	
//	public StudentResponse saveStudent(StudentCreateRequest studentRequest){
//
//	    log.info("Starting to save student with request: {}", studentRequest);
//
//	    // Validate the request
//	    if (validatorRuleService.isRuleActive(ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName())) {
//	        log.info("Starting {}", ValidatorRulesEnum.STUDENT_REQUEST_VALIDATOR.getRuleName());
//	        Validator<StudentCreateRequest> validator = applicationContext.getBean(StudentRequestValidator.class);
//	        validator.validate(studentRequest);
//	    }
//
//	    log.debug("Mapping StudentRequest to Student entity.");
//	    Student newStudent = modelMapper.map(studentRequest, Student.class);
//
//	        // Fetch department dynamically
//	        log.info("Fetching department with ID: {}", studentRequest.getDepartmentId());
//	        Department department = departmentRepository.findById(studentRequest.getDepartmentId())
//	                .orElseThrow(() -> {
//	                    log.error("Department not found for ID: {}", studentRequest.getDepartmentId());
//	                    return new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(studentRequest.getDepartmentId())   );
//	                });
//
//	        // Fetch faculty dynamically
//	        log.info("Fetching faculty with ID: {}", studentRequest.getMentorId());
//	        Faculty faculty = facultyRepository.findById(studentRequest.getMentorId())
//	                .orElseThrow(() -> {
//	                    log.error("Faculty not found for ID: {}", studentRequest.getMentorId());
//	                    return new ResourceNotFoundException("Faculty not found for ID: " + studentRequest.getMentorId());
//	                });
//
//	        // Set department and faculty in the student entity
//	        log.debug("Setting department and faculty for the student entity.");
//	        newStudent.setDepartment(department);
//	        newStudent.setFaculty(faculty);
//
//	        try {
//	        // Save the student entity
//	        log.info("Saving the student entity with student ID: {}", newStudent.getStudentId());
//	        Student savedStudent = studentRepository.save(newStudent);
//
//	        // Map the saved student to response
//	        log.debug("Mapping saved student entity to StudentResponse.");
//	        StudentResponse studentResponse = modelMapper.map(savedStudent, StudentResponse.class);
//	        studentResponse.setDepartmentId(savedStudent.getDepartment().getDepartmentId());
//	        studentResponse.setMentorId(savedStudent.getFaculty().getFacultyId());
//
//	        log.info("Student saved successfully with ID: {}", savedStudent.getStudentId());
//	        return studentResponse;
//
//	    } catch (Exception e) {
//	        log.error("Unexpected error saving student: {}", e.getMessage());
//	        throw e;
//	    }
//	}
//	
//	
//
//	
//	
//	
//	@Override
//	public Semester getSemester(String semesterCode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	 public SemesterResponse saveSemester(SemBasicDetailsRes semesterBasicDataRequest) {
//		
//		if(semesterRepository.existsById(semesterBasicDataRequest.getSemesterCode())) {
//			throw  new ResourceNotFoundException(ErrorMessageEnum.DUPLICATE_SEMESTER_CODE.getMessage(semesterBasicDataRequest.getSemesterCode()));
//}
//		Department department = departmentRepository.findById(semesterBasicDataRequest.getDepartmentId())
//				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(semesterBasicDataRequest.getDepartmentId()))); 
//		
//        // Create a new Semester entity
//        Semester newSemester = new Semester();
//        newSemester.setSemesterCode(semesterBasicDataRequest.getSemesterCode());
//        newSemester.setSemesterNumber(semesterBasicDataRequest.getSemesterNumber());
//        newSemester.setStudyYear(semesterBasicDataRequest.getStudyYear());
//        newSemester.setAcademicYear(semesterBasicDataRequest.getAcademicYear());
//        newSemester.setRegulation(semesterBasicDataRequest.getRegulation());
//        newSemester.setStartDate(semesterBasicDataRequest.getStartDate());
//        newSemester.setEndDate(semesterBasicDataRequest.getEndDate());
//        newSemester.setBatch(semesterBasicDataRequest.getBatch());
//        newSemester.setDepartmentId(department.getDepartmentId()); // Set the department
//
////        // Fetch and set students for the semester 
////        List<Student> students = studentRepository.findAllByStudentIdIn(semesterBasicDataRequest.getStudents());
////        newSemester.setStudents(students);
////
////        // Fetch and set faculties for the semester
////        List<Faculty> faculties = facultyRepository.findAllByFacultyIdIn(semesterBasicDataRequest.getFaculties());
////        newSemester.setFaculties(faculties);
////
////        // Save the semester
////        log.info("Saving semester data into DB");
//        Semester savedSemester = semesterRepository.save(newSemester);
//        
// 
//        
//
//        // Return response (you can map it to a response DTO if necessary)
//        return null;//new SemesterResponse(savedSemester);
//    }
//
//
//	
//
//	@Override
//	public SemesterDetails getSemesterDetails(String semesterCode) {
//		
//		List<String> students =new  ArrayList<>();
//		List<String> faculties =new  ArrayList<>();
//		List<String> semesterSubjects =new  ArrayList<>();
//		Semester semester = semesterRepository.findById(semesterCode).orElseThrow( () -> new  ResourceNotFoundException(ErrorMessageEnum.DUPLICATE_SEMESTER_CODE.getMessage(semesterCode)));
//		SemesterDetails semesterResponse = modelMapper.map(semester, SemesterDetails.class);
//		
//		//semesterRepository.f
//		
//		log.info("Fetched semester deails: {}", semesterResponse);
//		return null;
//	}
//	
//		
//		
//
//}
//


package com.sts.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.sts.dto.semester.AddFacultiesToSemReq;
import com.sts.dto.semester.AddFacultiesToSemRes;
import com.sts.dto.semester.AddStudentsToSemReq;
import com.sts.dto.semester.AddStudentsToSemRes;
import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;
import com.sts.dto.semester.AddSubjectsToSemReq;
import com.sts.dto.semester.AddSubjectsToSemRes;
import com.sts.dto.semester.SemBasicDetailsReq;
import com.sts.dto.semester.SemBasicDetailsRes;
import com.sts.dto.semester.SemesterCreateRequest;
import com.sts.dto.semester.SemesterDetails;
import com.sts.dto.semester.SemesterOverallDetailsRes;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Semester;
import com.sts.entity.SemesterFaculty;
import com.sts.entity.SemesterStudent;
import com.sts.entity.SemesterSubject;
import com.sts.entity.Student;
import com.sts.entity.StudentSubject;
import com.sts.entity.Subjects;
import com.sts.exceptions.ResourceNotFoundException;
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
import com.sts.service.interfaces.SemesterService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SemesterServiceImpl implements SemesterService {
	
	private final StudentRepository studentRepository;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final ApplicationContext applicationContext;
	private final ValidatorRuleStatus validatorRuleService;
	private final SemesterRepository semesterRepository;
	private final SubjectRepository subjectRepository;
	private final SemesterFacultyRepository semesterFacultyRepository;
	private final SemesterStudentRepository semesterStudentRepository; 
	private final SemesterSubjectRepository semesterSubjectRepository;
	
	private final ModelMapper modelMapper;
	private final StudentSubjectRepository studentSubjectRepository;

	public SemesterServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleService, SemesterRepository semesterRepository, SubjectRepository subjectRepository, SemesterFacultyRepository semesterFacultyRepository, SemesterStudentRepository semesterStudentRepository, SemesterSubjectRepository semesterSubjectRepository, StudentSubjectRepository studentSubjectRepository) {
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
	
	@Override
	public SemesterDetails getSemesterDetails(String semesterCode) {
		
		log.info("Fetching semester with code: {}", semesterCode);

		Semester semester = semesterRepository.findById(semesterCode).orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(semesterCode)));
		log.info("Semester Details: {}", semester.toString());
		
		SemesterFaculty semesterFaculty;

		SemesterDetails semesterDetails = modelMapper.map(semester, SemesterDetails.class);
		
		
		return semesterDetails;
	}
	
	
	
	@Override
	@Transactional
	public String createBulkSemesters(List<SemBasicDetailsReq> semesterRequests) {
	    
	    log.info("Starting bulk semester creation. Total records: {}", semesterRequests.size());

	    // Step 1: Validate and prefetch department IDs
	    Set<String> departmentIds = semesterRequests.stream()
	            .map(SemBasicDetailsReq::getDepartmentId)
	            .collect(Collectors.toSet());

	    Map<String, Department> departmentMap = departmentRepository.findAllById(departmentIds).stream()
	            .collect(Collectors.toMap(Department::getDepartmentId, Function.identity()));

	    // Step 2: Map requests to Semester entities and assign departments
	    List<Semester> semestersToSave = semesterRequests.stream()
	            .map(req -> {
	                Department department = departmentMap.get(req.getDepartmentId());
	                if (department == null) {
	                    throw new ResourceNotFoundException(
	                        ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(req.getDepartmentId())
	                    );
	                }

	                Semester semester = modelMapper.map(req, Semester.class);
	                semester.setDepartment(department);

	                return semester;
	            })
	            .collect(Collectors.toList());

	    // Step 3: Save all semesters
	    List<Semester> savedSemesters = semesterRepository.saveAll(semestersToSave);

	    // Step 4: Map saved entities to response DTOs
	    List<SemBasicDetailsRes> responses = savedSemesters.stream()
	            .map(semester -> modelMapper.map(semester, SemBasicDetailsRes.class))
	            .collect(Collectors.toList());

	    log.info("Successfully saved {} semesters.", responses.size());

	    return "Created "+responses.size()+" semesters";
	}


	
	@Override
	public SemesterOverallDetailsRes getOverallSemesterDetails(String semesterCode) {
	    log.info("Fetching overall semester details for: {}", semesterCode);

	    Semester semester = semesterRepository.findById(semesterCode)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(semesterCode)));

	    SemesterOverallDetailsRes res = new SemesterOverallDetailsRes();
	    res.setSemesterCode(semester.getSemesterCode());
	    res.setSemesterNumber(semester.getSemesterNumber());
	    res.setStudyYear(semester.getStudyYear());
	    res.setSemesterSerialNumber(semester.getSemesterSerialNumber());
	    res.setAcademicYear(semester.getAcademicYear());
	    res.setRegulation(semester.getRegulation());
	    res.setStartDate(semester.getStartDate());
	    res.setEndDate(semester.getEndDate());
	    res.setBatch(semester.getBatch());

	    Department dept = semester.getDepartment();
	    res.setDepartmentId(dept.getDepartmentId());
	    res.setDepartmentName(dept.getDepartmentName());

	    // Faculties
	    List<SemesterOverallDetailsRes.FacultyDetails> facultyDetails = semester.getSemesterFaculties().stream().map(faculty -> {
	        SemesterOverallDetailsRes.FacultyDetails details = new SemesterOverallDetailsRes.FacultyDetails();

	        if (faculty != null) {
	            details.setFacultyId(faculty.getFaculty().getFacultyId());
	            details.setFacultyName(faculty.getFaculty().getFacultyName());
	        } else {
	            log.warn("Encountered null faculty in semester: {}", semester.getSemesterCode());
	            details.setFacultyId(null);
	            details.setFacultyName(null);
	        }

	        return details;
	    }).collect(Collectors.toList());
	    res.setFaculties(facultyDetails);
	    
	    // Students
	    List<SemesterOverallDetailsRes.StudentDetails> studentDetails = semester.getSemesterStudents().stream().map(student -> {
	        SemesterOverallDetailsRes.StudentDetails details = new SemesterOverallDetailsRes.StudentDetails();

	        if (student != null) {
	            details.setStudentId(student.getStudent().getStudentId());
	            details.setStudentName(student.getStudent().getStudentName());
	        } else {
	            log.warn("Encountered null student in semester: {}", semester.getSemesterCode());
	            details.setStudentId(null);
	            details.setStudentName(null);
	        }

	        return details;
	    }).collect(Collectors.toList());
	    res.setStudents(studentDetails);

	    // Subjects
	    List<SemesterOverallDetailsRes.SubjectDetail> subjectDetails = semester.getSemesterSubjects().stream().map(ss -> {
	        SemesterOverallDetailsRes.SubjectDetail subjectDetail = new SemesterOverallDetailsRes.SubjectDetail();

	        if (ss.getSubject() != null) {
	            subjectDetail.setSubjectId(ss.getSubject().getSubjectId());
	        } else {
	            log.warn("Subject is null for subjectCode: {}", ss.getSubjectCode());
	            subjectDetail.setSubjectId(null);
	        }

	        subjectDetail.setSubjectCode(ss.getSubjectCode());
	        subjectDetail.setSubjectType(ss.getSubjectType());
	        subjectDetail.setCredits(ss.getCredits());

	        Faculty faculty = ss.getFaculty();
	        if (faculty != null) {
	            subjectDetail.setFacultyId(faculty.getFacultyId());
	        } else {
	            log.warn("Faculty is null for subjectCode: {}", ss.getSubjectCode());
	            subjectDetail.setFacultyId(null);
	        }

	        return subjectDetail;
	    }).collect(Collectors.toList());

	    res.setSubjects(subjectDetails); 

	    log.info("Successfully fetched overall semester details for: {}", semesterCode);
	    return res;
	}


	@Override
	public SemesterDetails getSemester(String semesterCode) {
		log.info("Fetching semester with code: {}", semesterCode);

		Semester semester = semesterRepository.findById(semesterCode).orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(semesterCode)));
		log.info("Semester Details: {}", semester.toString());

		SemesterDetails semesterDetails = modelMapper.map(semester, SemesterDetails.class);
		
		List<SemesterFaculty> semesterFaculty = semester.getSemesterFaculties();
		List<String> semesterFacultyIds = 
		semesterFaculty.stream().map(faculty -> faculty.getFaculty().getFacultyId()).collect(Collectors.toList());
		semesterDetails.setFacultyIds(semesterFacultyIds);
		
		List<SemesterStudent> semesterStudents = semester.getSemesterStudents();
		List<String> semesterStudentIds = 
				semesterStudents.stream().map(student -> student.getStudent() .getStudentId()).collect(Collectors.toList());
		semesterDetails.setStudentIds(semesterStudentIds);
		
		List<SemesterSubject> semesterSubjects = semester.getSemesterSubjects();
		
		List<String> semesterSubjectCodes = 
				semesterSubjects.stream().map(subject -> subject.getSubjectCode()).collect(Collectors.toList());
		semesterDetails.setSemesterSubjectCodes(semesterSubjectCodes);
		
		return semesterDetails;
	}

	@Override
	public SemBasicDetailsRes saveSemester(SemBasicDetailsReq semesterRequest) {
		
		log.info("Fetching department with ID: {}", semesterRequest.getDepartmentId());
		Department department = departmentRepository.findById(semesterRequest.getDepartmentId())
				.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.DEPARTMENT_ID_NOT_FOUND.getMessage(semesterRequest.getDepartmentId()))
						);
		Semester newSemester = modelMapper.map(semesterRequest, Semester.class);
		
		Semester savedSemester = semesterRepository.save(newSemester);
		 
		SemBasicDetailsRes semesterResponse = modelMapper.map(savedSemester, SemBasicDetailsRes.class);
		
		return semesterResponse;
	}
	
	@Override
	@Transactional
	public List<AddFacultiesToSemRes> addFacultiesToSemester(List<AddFacultiesToSemReq> reqList) {
	    log.info("Starting to assign faculties to semesters. Total records: {}", reqList.size());

	    if (reqList == null || reqList.isEmpty()) {
	        throw new IllegalArgumentException("Input list cannot be null or empty.");
	    }

	    // Validate inputs and collect IDs
	    Set<String> semesterCodes = new HashSet<>();
	    Set<String> facultyIds = new HashSet<>();

	    for (AddFacultiesToSemReq mapping : reqList) {
	        if (mapping.getSemesterCode() == null || mapping.getFacultyId() == null) {
	            throw new IllegalArgumentException("SemesterCode and FacultyId cannot be null.");
	        }
	        semesterCodes.add(mapping.getSemesterCode());
	        facultyIds.add(mapping.getFacultyId());
	    }

	    log.debug("Unique semester codes: {}", semesterCodes);
	    log.debug("Unique faculty IDs: {}", facultyIds);

	    // Prefetch related entities in bulk
	    Map<String, Semester> semesterMap = semesterRepository.findAllById(semesterCodes)
	            .stream()
	            .collect(Collectors.toMap(Semester::getSemesterCode, Function.identity()));

	    Map<String, Faculty> facultyMap = facultyRepository.findAllById(facultyIds)
	            .stream()
	            .collect(Collectors.toMap(Faculty::getFacultyId, Function.identity()));

	    List<AddFacultiesToSemRes> results = new ArrayList<>();

	    for (AddFacultiesToSemReq req : reqList) {
	        String semesterCode = req.getSemesterCode();
	        String facultyId = req.getFacultyId();

	        Semester semester = semesterMap.get(semesterCode);
	        if (semester == null) {
	            log.error("Semester not found for code: {}", semesterCode);
	            throw new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(semesterCode));
	        }

	        Faculty faculty = facultyMap.get(facultyId);
	        if (faculty == null) {
	            log.error("Faculty not found for ID: {}", facultyId);
	            throw new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(facultyId));
	        }

	        boolean alreadyMapped = semester.getSemesterFaculties().stream()
	                .anyMatch(sf -> sf.getFaculty().getFacultyId().equals(facultyId));

	        if (!alreadyMapped) {
	            SemesterFaculty semesterFaculty = new SemesterFaculty();
	            semesterFaculty.setSemester(semester);
	            semesterFaculty.setFaculty(faculty);
	            semester.getSemesterFaculties().add(semesterFaculty);

	            log.info("faculty {} assigned to semester {}", facultyId, semesterCode);
	        } else {
	            throw new ResourceNotFoundException("faculty " +facultyId+" already assigned to "+semesterCode+" semester ");
	        }

	        // Prepare response DTO
	        AddFacultiesToSemRes res = new AddFacultiesToSemRes();
	        res.setSemesterCode(semesterCode);
	        res.setFacultyId(facultyId);
	        results.add(res);
	    }

	    semesterRepository.saveAll(semesterMap.values());
	    log.info("Successfully processed {} faculty-semester assignements.", reqList.size());

	    return results;
	}


	
	@Override
	@Transactional
	public List<AddStudentsToSemRes> addStudentsToSemester(List<AddStudentsToSemReq> reqList) {
	    log.info("Starting to add students to semesters. Total records: {}", reqList.size());

	    if (reqList == null || reqList.isEmpty()) {
	        throw new IllegalArgumentException("Input list cannot be null or empty.");
	    }

	    // Collect unique semester codes and student IDs
	    Set<String> semesterCodes = reqList.stream()
	        .map(AddStudentsToSemReq::getSemesterCode)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    Set<String> studentIds = reqList.stream()
	        .map(AddStudentsToSemReq::getStudentId)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    if (semesterCodes.isEmpty() || studentIds.isEmpty()) {
	        throw new IllegalArgumentException("Semester codes and student IDs cannot be null or empty.");
	    }

	    // Fetch semesters and students in bulk
	    Map<String, Semester> semesterMap = semesterRepository.findAllById(semesterCodes).stream()
	        .collect(Collectors.toMap(Semester::getSemesterCode, Function.identity()));

	    Map<String, Student> studentMap = studentRepository.findAllById(studentIds).stream()
	        .collect(Collectors.toMap(Student::getStudentId, Function.identity()));

	    // Validate existence
	    for (String code : semesterCodes) {
	        if (!semesterMap.containsKey(code)) {
	            log.error("Semester not found with code: {}", code);
	            throw new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(code));
	        }
	    }

	    for (String id : studentIds) {
	        if (!studentMap.containsKey(id)) {
	            log.error("Student not found with ID: {}", id);
	            throw new ResourceNotFoundException(ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(id));
	        }
	    }

	    List<AddStudentsToSemRes> results = new ArrayList<>();

	    for (AddStudentsToSemReq req : reqList) {
	        Semester semester = semesterMap.get(req.getSemesterCode());
	        Student student = studentMap.get(req.getStudentId());

	        boolean alreadyMapped = semester.getSemesterStudents().stream()
	            .anyMatch(ss -> ss.getStudent().getStudentId().equals(req.getStudentId()));

	        if (!alreadyMapped) {
	            SemesterStudent semesterStudent = new SemesterStudent();
	            semesterStudent.setSemester(semester);
	            semesterStudent.setStudent(student);
	            semester.getSemesterStudents().add(semesterStudent);

	            log.info("Mapped student {} to semester {}", req.getStudentId(), req.getSemesterCode());
	        } else {
	            log.warn("Student {} already mapped to semester {}, skipping", req.getStudentId(), req.getSemesterCode());
	            throw new ResourceNotFoundException("Student " + req.getStudentId()+" already assigned to "+" semester "+req.getSemesterCode());
	        }

	        AddStudentsToSemRes res = new AddStudentsToSemRes();
	        res.setSemesterCode(req.getSemesterCode());
	        res.setStudentId(req.getStudentId());
	        results.add(res);
	    }

	    semesterRepository.saveAll(semesterMap.values());
	    log.info("Successfully added students to semesters. Total mappings: {}", results.size());

	    return results;
	}


	
	@Override
	@Transactional
	public List<AddSubjectsToSemRes> addSubjectsToSemester(List<AddSubjectsToSemReq> reqList) {
	    log.info("Mapping subjects to semesters. Total requests: {}", reqList.size());

	    if (reqList == null || reqList.isEmpty()) {
	        throw new IllegalArgumentException("Input list cannot be null or empty.");
	    }

	    // Collect unique semesterCodes, facultyIds, and subjectIds
	    Set<String> semesterCodes = reqList.stream()
	        .map(AddSubjectsToSemReq::getSemesterCode)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    Set<String> facultyIds = reqList.stream()
	        .map(AddSubjectsToSemReq::getFacultyId)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    Set<String> subjectIds = reqList.stream()
	        .map(AddSubjectsToSemReq::getSubjectId)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toSet());

	    // Validate inputs
	    if (semesterCodes.isEmpty() || facultyIds.isEmpty() || subjectIds.isEmpty()) {
	        throw new IllegalArgumentException("Semester codes, faculty IDs, and subject IDs cannot be null or empty.");
	    }

	    // Bulk fetch entities
	    Map<String, Semester> semesterMap = semesterRepository.findAllById(semesterCodes).stream()
	        .collect(Collectors.toMap(Semester::getSemesterCode, Function.identity()));

	    Map<String, Faculty> facultyMap = facultyRepository.findAllById(facultyIds).stream()
	        .collect(Collectors.toMap(Faculty::getFacultyId, Function.identity()));

	    Map<String, Subjects> subjectMap = subjectRepository.findAllById(subjectIds).stream()
	        .collect(Collectors.toMap(Subjects::getSubjectId, Function.identity()));

	    // Validate existence
	    semesterCodes.forEach(code -> {
	        if (!semesterMap.containsKey(code)) {
	            log.error("Semester not found: {}", code);
	            throw new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(code));
	        }
	    });

	    facultyIds.forEach(id -> {
	        if (!facultyMap.containsKey(id)) {
	            log.error("Faculty not found: {}", id);
	            throw new ResourceNotFoundException(ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(id));
	        }
	    });

	    subjectIds.forEach(id -> {
	        if (!subjectMap.containsKey(id)) {
	            log.error("Subject not found: {}", id);
	            throw new ResourceNotFoundException(ErrorMessageEnum.SUBJECT_ID_NOT_FOUND.getMessage(id));
	        }
	    });

	    List<AddSubjectsToSemRes> results = new ArrayList<>();

	    for (AddSubjectsToSemReq req : reqList) {
	        Semester semester = semesterMap.get(req.getSemesterCode());

	        boolean alreadyExists = semester.getSemesterSubjects().stream()
	            .anyMatch(ss -> ss.getSubjectCode().equals(req.getSubjectCode()));

	        if (!alreadyExists) {
	            SemesterSubject semesterSubject = new SemesterSubject();
	            semesterSubject.setSemester(semester);
	            semesterSubject.setSubjectCode(req.getSubjectCode());
	            semesterSubject.setSubjectType(req.getSubjectType());
	            semesterSubject.setCredits(req.getCredits());
	            semesterSubject.setSubject(subjectMap.get(req.getSubjectId()));
	            semesterSubject.setFaculty(facultyMap.get(req.getFacultyId()));

	            semester.getSemesterSubjects().add(semesterSubject);

	            log.info("Mapped subject {} (ID {}) to semester {}", req.getSubjectCode(), req.getSubjectId(), req.getSemesterCode());
	        } else {
	            throw new ResourceNotFoundException("Student " + req.getSubjectCode()+" already assigned to semester "+req.getSemesterCode());
	        }

	        AddSubjectsToSemRes res = new AddSubjectsToSemRes();
	        res.setSemesterCode(req.getSemesterCode());
	        res.setSubjectCode(req.getSubjectCode());
	        results.add(res);
	    }

	    semesterRepository.saveAll(semesterMap.values());
	    log.info("Successfully mapped {} subjects to semesters.", results.size());

	    return results;
	}


	@Override
	@Transactional
	public List<AddStudentsToSubjectRes> addStudentsToSubject(List<AddStudentsToSubjectReq> reqList) {
	    log.info("Starting to add students to subjects. Total records: {}", reqList.size());

	    if (reqList == null || reqList.isEmpty()) {
	        throw new IllegalArgumentException("Request list cannot be null or empty.");
	    }

	    // Collect unique subject codes and student IDs for bulk fetch
	    Set<String> subjectCodes = reqList.stream()
	                                      .map(AddStudentsToSubjectReq::getSubjectCode)
	                                      .collect(Collectors.toSet());

	    Set<String> studentIds = reqList.stream()
	                                    .map(AddStudentsToSubjectReq::getStudentId)
	                                    .collect(Collectors.toSet());

	    // Bulk fetch SemesterSubjects
	    Map<String, SemesterSubject> semesterSubjectMap = semesterSubjectRepository.findAllById(subjectCodes)
	            .stream()
	            .collect(Collectors.toMap(SemesterSubject::getSubjectCode, Function.identity()));

	    // Bulk fetch Students
	    Map<String, Student> studentMap = studentRepository.findAllById(studentIds)
	            .stream()
	            .collect(Collectors.toMap(Student::getStudentId, Function.identity()));

	    List<AddStudentsToSubjectRes> results = new ArrayList<>();

	    for (AddStudentsToSubjectReq req : reqList) {
	        String subjectCode = req.getSubjectCode();
	        String studentId = req.getStudentId();

	        SemesterSubject semesterSubject = semesterSubjectMap.get(subjectCode);
	        if (semesterSubject == null) {
	            log.error("Subject not found with code: {}", subjectCode);
	            throw new ResourceNotFoundException(ErrorMessageEnum.SUBJECT_ID_NOT_FOUND.getMessage(subjectCode));
	        }

	        Student student = studentMap.get(studentId);
	        if (student == null) {
	            log.error("Student not found with ID: {}", studentId);
	            throw new ResourceNotFoundException("Student not found: " + studentId);
	        }

	        boolean alreadyMapped = semesterSubject.getStudents().stream()
	            .anyMatch(ss -> ss.getStudent().getStudentId().equals(studentId));

	        if (!alreadyMapped) {
	            StudentSubject studentSubject = new StudentSubject();
	            studentSubject.setStudent(student);
	            studentSubject.setSemesterSubject(semesterSubject);
	            studentSubjectRepository.save(studentSubject);

	            log.info("Mapped student {} to subject {}", studentId, subjectCode);

	            AddStudentsToSubjectRes res = new AddStudentsToSubjectRes(subjectCode, studentId);
	            results.add(res);
	        } else {
	            throw new ResourceNotFoundException("Student " + studentId+" already assigned to subject "+subjectCode);
	        }
	    }

	    return results;
	}


}



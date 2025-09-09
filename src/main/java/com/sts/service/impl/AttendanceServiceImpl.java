package com.sts.service.impl;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.attendance.AddAttendanceRequest;
import com.sts.dto.attendance.AttendanceRequest;
import com.sts.dto.attendance.AttendanceResponse;
import com.sts.dto.attendance.AttendanceUpdateRequest;
import com.sts.dto.attendance.GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes;
import com.sts.dto.attendance.GetActiveSemesterAttendanceByStudentIdRes;
import com.sts.dto.attendance.GetAttendanceByStudentIdAndSubjectCodeReq;
import com.sts.dto.attendance.GetLowAttendanceStudentsByFacultyIdRes;
import com.sts.dto.attendance.GetStudentAllSemesterAttendanceRes;
import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;
import com.sts.dto.attendance.StudentAttendanceSummary;
import com.sts.dto.attendance.SubjectAttendance;
import com.sts.entity.Attendance;
import com.sts.entity.Department;
import com.sts.entity.Semester;
import com.sts.entity.SemesterSubject;
import com.sts.entity.Student;
import com.sts.entity.StudentSubject;
import com.sts.exceptions.CustomException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.AttendanceRepository;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.SemesterSubjectRepository;
import com.sts.repository.StudentRepository;
import com.sts.repository.StudentSubjectRepository;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.AttendanceService;
import com.sts.service.interfaces.StudentSubjectService;
import com.sts.specification.AttendanceSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final ModelMapper modelMapper;
	private final StudentRepository studentRepository;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final SemesterRepository semesterRepository;
	private final ValidatorRuleStatus validatorRuleService;
	private final ApplicationContext applicationContext;
	private final SemesterSubjectRepository semesterSubjectRepository;
	private final StudentSubjectRepository studentSubjectRepository;
	private final StudentSubjectService studentSubjectService;


	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ModelMapper modelMapper,
			StudentRepository studentRepository, FacultyRepository facultyRepository,
			DepartmentRepository departmentRepository, SemesterRepository semesterRepository,
			ValidatorRuleStatus validatorRuleService, ApplicationContext applicationContext, SemesterSubjectRepository semesterSubjectRepository, StudentSubjectRepository studentSubjectRepository, StudentSubjectService studentSubjectService) {
		super();
		this.attendanceRepository = attendanceRepository;
		this.modelMapper = modelMapper;
		this.studentRepository = studentRepository;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.semesterRepository = semesterRepository;
		this.validatorRuleService = validatorRuleService;
		this.applicationContext = applicationContext;
		this.semesterSubjectRepository = semesterSubjectRepository;
		this.studentSubjectRepository = studentSubjectRepository;
		this.studentSubjectService = studentSubjectService;
	}


	@Override
	public List<AttendanceResponse> getAttendance(AttendanceRequest filterRequest) {
		log.info("Looking up attendance records with the following filters: {}", filterRequest);

		try {
			// Use Specification to find matching attendance records
			List<Attendance> attendanceList = attendanceRepository.findAll(AttendanceSpecification.getAttendanceSpec(filterRequest));

			if (attendanceList.isEmpty()) {
				log.info("No attendance records found matching the criteria.");
				return Collections.emptyList();
			}

			// Map entities to response DTOs
			List<AttendanceResponse> responseList = attendanceList.stream()
					.map(attendance -> {
						AttendanceResponse response = modelMapper.map(attendance, AttendanceResponse.class);
						response.setStudentId(attendance.getStudent().getStudentId());
						response.setSemesterCode(attendance.getSemester().getSemesterCode());
						return response;
					})
					.collect(Collectors.toList());

			log.info("Successfully fetched {} attendance records.", responseList.size());
			return responseList;

		} catch (Exception e) {
			log.error("An error occurred while fetching attendance records: {}", e.getMessage());
			throw new CustomException("Unexpected error occurred while fetching attendance records", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public AttendanceResponse createAttendance(AttendanceRequest attendanceRequest) {

		log.info("Received a request to save attendance with the following details: {}", attendanceRequest);


		//	        // Check if the validation rule is active
		//	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
		//	            log.info("The attendance validation rule is active. Let's validate the request.");
		//	            Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);
		//	            validator.validate(attendanceRequest);
		//	            log.info("Validation passed for this attendance request: {}", attendanceRequest);
		//	        } else {
		//	            log.warn("Validation rule is not active, skipping the validation step.");
		//	        }

		// Mapping the request to an entity and fetching the related student and semester
		log.debug("Mapping the attendance request to an Attendance entity.");
		Attendance newAttendance = modelMapper.map(attendanceRequest, Attendance.class);

		// Fetch the student and semester based on the request
		log.info("Fetching student with ID: {}", attendanceRequest.getStudentId());
		//	        Student student = studentRepository.findById(attendanceRequest.getStudentId()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(attendanceRequest.getStudentId())));
		//	       
		StudentSubject studentSubject = studentSubjectRepository.getBySubjectCodeAndStudentId(attendanceRequest.getSubjectCode(), attendanceRequest.getStudentId()).orElseThrow(
				() -> new ResourceNotFoundException(ErrorMessageEnum.STUDENT_SEMESTER_MISSMATCH
						.getMessage(attendanceRequest.getStudentId(), attendanceRequest.getSemesterCode())));

		Student student = studentSubject.getStudent();
		log.info("Student {} of Sem  {}", student.getStudentId(),studentSubject.getSemesterSubject().getSemester().getSemesterCode() );

		newAttendance.setStudent(student);

		//	        log.info("Fetching semester for the code: {}", attendanceRequest.getSemesterCode());
		//	        Semester semester = semesterRepository.findById(attendanceRequest.getSemesterCode()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.SEMESTER_CODE_NOT_FOUND.getMessage(attendanceRequest.getSemesterCode())));
		//	        
		SemesterSubject semesterSubject = semesterSubjectRepository.findById(attendanceRequest.getSubjectCode())				 
				.orElseThrow(() -> new ResourceNotFoundException(
						ErrorMessageEnum.SUBJECT_ID_NOT_FOUND.getMessage(attendanceRequest.getSubjectCode())));

		Semester semester = semesterSubject.getSemester();
		Department department = semester.getDepartment();


		newAttendance.setSemester(semester);
		newAttendance.setDepartment(department);
		newAttendance.setSemesterSubject(semesterSubject);
		newAttendance.setStudent(student);

		// Save the attendance entity
		log.info("Saving the attendance record.");
		Attendance savedAttendance = attendanceRepository.save(newAttendance);
		log.info("Attendance saved successfully");

		// Prepare the response DTO
		AttendanceResponse attendanceResponse = modelMapper.map(savedAttendance, AttendanceResponse.class);
		attendanceResponse.setStudentId(savedAttendance.getStudent().getStudentId());
		attendanceResponse.setSemesterCode(savedAttendance.getSemester().getSemesterCode());

		log.info("Returning the saved attendance record.");
		return attendanceResponse;


	}

	@Override
	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest) {
		log.info("Received a request to update attendance for student ID: {} and subject: {} on date: {} for period: {}", 
				attendanceUpdateRequest.getStudentId(), 
				attendanceUpdateRequest.getSubjectCode(), 
				attendanceUpdateRequest.getAttendanceDate(),
				attendanceUpdateRequest.getPeriod());

		try {
			// Map the attendance update request to the Attendance entity
			Attendance updatedAttendance = modelMapper.map(attendanceUpdateRequest, Attendance.class);

			// Call the repository method to update the attendance status
			int response = attendanceRepository.updateAttendance(
					attendanceUpdateRequest.getIsPresent(),
					attendanceUpdateRequest.getStudentId(),
					attendanceUpdateRequest.getSubjectCode(),
					attendanceUpdateRequest.getAttendanceDate(),
					attendanceUpdateRequest.getPeriod()
					);

			if (response > 0) {
				log.info("Successfully updated attendance for student ID: {} and subject: {}", 
						attendanceUpdateRequest.getStudentId(), attendanceUpdateRequest.getSubjectCode());
				return "Successfully updated attendance for student ID: " + attendanceUpdateRequest.getStudentId();
			} else {
				String errorMessage = "No records found to update attendance for student ID: " 
						+ attendanceUpdateRequest.getStudentId();
				log.error(errorMessage);
				throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("An error occurred while updating attendance for student ID: {}: {}", 
					attendanceUpdateRequest.getStudentId(), e.getMessage());
			throw new CustomException("Error updating attendance", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override

	public String saveSubjectStudentsAttendance(List<AttendanceRequest> req) {
		log.info("Received a request to save multiple attendance records, total records: {}", req.size());

		List<Attendance> attendanceList  = new ArrayList<>();

		for (AttendanceRequest request : req) {
			log.info("Processing attendance request for student ID: {} and subject code: {}", request.getStudentId(), request.getSubjectCode());

			// Map basic fields(date,period,present) from request to entity
			Attendance newAttendance = modelMapper.map(request, Attendance.class);

			// Get StudentSubject for validation and fetch student
			StudentSubject studentSubject = studentSubjectRepository.getBySubjectCodeAndStudentId(request.getSubjectCode(), request.getStudentId())
					.orElseThrow(() -> new ResourceNotFoundException(ErrorMessageEnum.STUDENT_SUBJECT_MISSMATCH
							.getMessage(request.getStudentId(), request.getSubjectCode())));

			Student student = studentSubject.getStudent();
			newAttendance.setStudent(student);

			// Get SemesterSubject and extract semester and department
			SemesterSubject semesterSubject = semesterSubjectRepository.findById(request.getSubjectCode())
					.orElseThrow(() -> new ResourceNotFoundException(
							ErrorMessageEnum.SUBJECT_ID_NOT_FOUND.getMessage(request.getSubjectCode())));

			Semester semester = semesterSubject.getSemester();
			Department department = semester.getDepartment();

			// Set semester, department, and subject reference
			newAttendance.setSemester(semester);
			newAttendance.setDepartment(department);
			newAttendance.setSemesterSubject(semesterSubject);

			attendanceList.add(newAttendance);
		}

		// Save all records
		log.info("Saving all attendance records");
		List<Attendance> savedAttendances = attendanceRepository.saveAll(attendanceList);
		log.info("Successfully saved {} attendance records.", savedAttendances.size());

		return "Successfully saved " + savedAttendances.size() + " attendance records";
	}




	@Override
	public String bulkCreateAttendances(List<AddAttendanceRequest> requestList) {
	    log.info("Received a request to save attendance batches, total batches: {}", requestList.size());

	    List<Attendance> attendanceList = new ArrayList<>();
	    
	    // Get today's date
	    LocalDate today = LocalDate.now();

	    for (AddAttendanceRequest request : requestList) {
	        log.info("Processing bulk attendance for subject: {} on date: {} period: {}",
	                request.getSubjectCode(), request.getAttendanceDate(), request.getPeriod());

	        // Validate the attendance date
	        LocalDate attendanceDate = LocalDate.parse(request.getAttendanceDate());
	        if (attendanceDate.isAfter(today)) {
	            // If the attendance date is beyond today, throw an exception
	            throw new ResourceNotFoundException(
	                "Attendance date cannot be in the future. Provided date: " + attendanceDate);
	        }

	        for (AddAttendanceRequest.StudentStatus studentStatus : request.getAttendanceData()) {
	            log.info("Processing student ID: {}", studentStatus.getStudentId());

	            // Create and populate Attendance entity
	            Attendance newAttendance = new Attendance();
	            newAttendance.setAttendanceDate(attendanceDate);  // Use validated date
	            newAttendance.setPeriod(request.getPeriod());
	            newAttendance.setIsPresent(studentStatus.getIsPresent());

	            // Fetch student-subject relationship
	            StudentSubject studentSubject = studentSubjectRepository
	                    .getBySubjectCodeAndStudentId(request.getSubjectCode(), studentStatus.getStudentId())
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            ErrorMessageEnum.STUDENT_SUBJECT_MISSMATCH
	                                    .getMessage(studentStatus.getStudentId(), request.getSubjectCode())));

	            Student student = studentSubject.getStudent();
	            newAttendance.setStudent(student);

	            // Fetch semester-subject and related entities
	            SemesterSubject semesterSubject = semesterSubjectRepository.findById(request.getSubjectCode())
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            ErrorMessageEnum.SUBJECT_ID_NOT_FOUND.getMessage(request.getSubjectCode())));

	            Semester semester = semesterSubject.getSemester();
	            Department department = semester.getDepartment();

	            newAttendance.setSemester(semester);
	            newAttendance.setDepartment(department);
	            newAttendance.setSemesterSubject(semesterSubject);

	            attendanceList.add(newAttendance);
	        }
	    }

	    // Save all attendance entries
	    List<Attendance> saved = attendanceRepository.saveAll(attendanceList);
	    log.info("Successfully saved {} attendance records", saved.size());

	    return "Successfully saved " + saved.size() + " attendance records";
	}





	@Override
	public String updateMultipleAttendance(List<AttendanceUpdateRequest> attendanceUpdateRequests) {
		log.info("Received a request to update multiple attendance records, total records: {}", attendanceUpdateRequests.size());

		int totalUpdated = 0;

		try {
			for (AttendanceUpdateRequest request : attendanceUpdateRequests) {
				log.debug("Updating attendance for student ID: {} and subject: {} on date: {} for period: {}", 
						request.getStudentId(), request.getSubjectCode(), request.getAttendanceDate(), request.getPeriod());

				int updated = attendanceRepository.updateAttendance(
						request.getIsPresent(),
						request.getStudentId(),
						request.getSubjectCode(),
						request.getAttendanceDate(),
						request.getPeriod()
						);

				if (updated > 0) {
					log.info("Successfully updated attendance for student ID: {} and subject: {}", 
							request.getStudentId(), request.getSubjectCode());
					totalUpdated++;
				} else {
					log.warn("No records found to update for student ID: {} and subject: {}", 
							request.getStudentId(), request.getSubjectCode());
				}
			}

			if (totalUpdated == attendanceUpdateRequests.size()) {
				log.info("Successfully updated all {} attendance records.", totalUpdated);
				return "Successfully updated " + totalUpdated + " attendance records";
			} else {
				String errorMessage = "Mismatch in records updated: expected " + attendanceUpdateRequests.size() + " but updated " + totalUpdated;
				log.error(errorMessage);
				throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.error("An error occurred while updating multiple attendance records: {}", e.getMessage());
			throw new CustomException("Error updating multiple attendance records", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String getAttendanceByStudentIdAndSubjectCode(GetAttendanceByStudentIdAndSubjectCodeReq req) {

		//List<Attendance> attendance = attendanceRepository.findAllByStudent_StudentIdAndSemesterSubject_SubjectCode(req.getStudentId(), req.getSubjectCode());






		return null;
	}


	@Override
	//Returns attendance of all students of the give subject.
	public List<GetStudentSemesterAttendanceRes> getAllStudentsSemesterAttendance(String subjectCode) {

		List<String> studentIds = studentSubjectService.getSubjectStudentsBySubjectCode(subjectCode);
		log.info("Student ids {}",studentIds);
		Map<String,GetStudentSemesterAttendanceRes> studentMap = new HashMap<>();

		List<Attendance> allStudentSemesterAttendanceList = attendanceRepository.findAllBySemesterSubject_SubjectCodeAndStudent_StudentIdIn(subjectCode, studentIds);


		for(Attendance attendance : allStudentSemesterAttendanceList) {

			String studentId = attendance.getStudent().getStudentId();

			if(!studentMap.containsKey(studentId)) {

				GetStudentSemesterAttendanceRes studentSemesterAttendance = new GetStudentSemesterAttendanceRes();
				studentSemesterAttendance.setStudentId(studentId);
				studentSemesterAttendance.setSubjectCode(attendance.getSemesterSubject().getSubjectCode());
				studentSemesterAttendance.setAttendancePercentage(0);
				studentSemesterAttendance.setTotalWorkingsDays(0);
				studentSemesterAttendance.setTotalDaysPresent(0);
				studentSemesterAttendance.setTotalDaysAbsent(0);

				studentMap.put(studentId, studentSemesterAttendance);
			}
			if(attendance.getIsPresent() == true) {
				int presentsCount = studentMap.get(studentId).getTotalDaysPresent();
				presentsCount++;
				studentMap.get(studentId).setTotalDaysPresent(presentsCount);
			}
			else {
				int absentsCount = studentMap.get(studentId).getTotalDaysAbsent();
				absentsCount++;
				studentMap.get(studentId).setTotalDaysAbsent(absentsCount);
			}




		}


		List<GetStudentSemesterAttendanceRes> res = studentMap.values()
				.stream()
				.sorted(Comparator.comparing(GetStudentSemesterAttendanceRes::getStudentId)) // optional
				.collect(Collectors.toList());

		for(GetStudentSemesterAttendanceRes filteredStudentAttendance : res) {
			int totalDaysPresent = filteredStudentAttendance.getTotalDaysPresent();
			int totalDaysAbsent = filteredStudentAttendance.getTotalDaysAbsent();
			int totalWorkingDays = totalDaysPresent + totalDaysAbsent;
			double attendancePercentage =  (totalDaysPresent * 100.0) / totalWorkingDays;
			double roundedPercentage = Math.round(attendancePercentage * 100.0) / 100.0;
			filteredStudentAttendance.setTotalWorkingsDays(totalWorkingDays);
			filteredStudentAttendance.setAttendancePercentage(roundedPercentage);
		}


		log.info("{}",res.get(0));

		return res;
	}


	@Override
	public GetStudentAllSemesterAttendanceRes getAttendancesByStudentId(String studentId) {
	    
		
		if(!studentRepository.existsByStudentId(studentId)) {
			throw new ResourceNotFoundException(ErrorMessageEnum.STUDENT_ID_NOT_FOUND.getMessage(studentId));
		}
	    List<Attendance> attendanceList = attendanceRepository.findAllByStudent_StudentId(studentId);

	    // Outer Map: semesterCode -> (subjectCode -> SubjectAttendance)
	    Map<String, Map<String, SubjectAttendance>> semesterSubjectMap = new HashMap<>();
	    Map<String, Integer> semesterSerialMap = new HashMap<>();

	    for (Attendance attendance : attendanceList) {
	        String semesterCode = attendance.getSemester().getSemesterCode();
	        int semesterSerialNumber = attendance.getSemester().getSemesterSerialNumber();
	        String subjectCode = attendance.getSemesterSubject().getSubjectCode();
	        String subjectTitle = attendance.getSemesterSubject().getSubject().getSubjectTitle();

	        // Store semesterSerialNumber (for sorting later)
	        semesterSerialMap.put(semesterCode, semesterSerialNumber);

	        // Create inner subject map if missing
	        semesterSubjectMap.putIfAbsent(semesterCode, new HashMap<>());
	        Map<String, SubjectAttendance> subjectMap = semesterSubjectMap.get(semesterCode);

	        // Get or create SubjectAttendance
	        SubjectAttendance subjectAttendance = subjectMap.computeIfAbsent(subjectCode, k -> {
	            SubjectAttendance sa = new SubjectAttendance();
	            sa.setSubjectCode(subjectCode);
	            sa.setSubjectTitle(subjectTitle);
	            sa.setTotalDaysPresent(0);
	            sa.setTotalDaysAbsent(0);
	            sa.setTotalWorkingsDays(0);
	            sa.setAttendancePercentage(0.0);
	            return sa;
	        });

	        // Count present/absent
	        if (Boolean.TRUE.equals(attendance.getIsPresent())) {
	            subjectAttendance.setTotalDaysPresent(subjectAttendance.getTotalDaysPresent() + 1);
	        } else {
	            subjectAttendance.setTotalDaysAbsent(subjectAttendance.getTotalDaysAbsent() + 1);
	        }
	    }

	    // Build response
	    GetStudentAllSemesterAttendanceRes response = new GetStudentAllSemesterAttendanceRes();
	    response.setStudentId(studentId);

	    List<GetStudentAllSemesterAttendanceRes.StudentSemesterSubjectAttendanceMap> semesters = new ArrayList<>();

	    for (Map.Entry<String, Map<String, SubjectAttendance>> semEntry : semesterSubjectMap.entrySet()) {
	        String semesterCode = semEntry.getKey();
	        int semesterSerial = semesterSerialMap.getOrDefault(semesterCode, 0);

	        GetStudentAllSemesterAttendanceRes.StudentSemesterSubjectAttendanceMap semMap =
	                new GetStudentAllSemesterAttendanceRes.StudentSemesterSubjectAttendanceMap();
	        semMap.setSemesterCode(semesterCode);
	        semMap.setSemesterSerialNumber(semesterSerial);

	        List<SubjectAttendance> subjectList = new ArrayList<>();

	        for (SubjectAttendance subject : semEntry.getValue().values()) {
	            int total = subject.getTotalDaysPresent() + subject.getTotalDaysAbsent();
	            subject.setTotalWorkingsDays(total);
	            double percentage = total == 0 ? 0.0 : (subject.getTotalDaysPresent() * 100.0) / total;
	            subject.setAttendancePercentage(Math.round(percentage * 100.0) / 100.0);

	            subjectList.add(subject);
	        }

	        semMap.setSemesterSubjectAttendanceList(subjectList);
	        semesters.add(semMap);
	    }

	    // Optional: sort semesters by serial number
	    semesters.sort(Comparator.comparing(GetStudentAllSemesterAttendanceRes.StudentSemesterSubjectAttendanceMap::getSemesterSerialNumber));

	    response.setSemesterSubjectsAttendance(semesters);
	    return response;
	}
	
	@Override
	public GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes getActiveSemesterAttendanceByStudentIdAndSemesterCode(String studentId, String semesterCode) {
	    List<Attendance> attendanceList = attendanceRepository
	            .findByStudent_StudentIdAndSemester_SemesterCode(studentId, semesterCode);
	    	Semester activeSemester = semesterRepository.findActiveSemesterByStudentIdAndDate(studentId, LocalDate.now()).orElseThrow();
	    	log.info(activeSemester.getSemesterCode());
	    	
	    	Map<String, List<Attendance>> groupedBySubject = attendanceList.stream()
	            .collect(Collectors.groupingBy(a -> a.getSemesterSubject().getSubjectCode()));

	    List<GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes.SubjectAttendance> subjectAttendances = new ArrayList<>();

	    int totalOverallPresent = 0;
	    int totalOverallWorkingDays = 0;

	    for (Map.Entry<String, List<Attendance>> entry : groupedBySubject.entrySet()) {
	        String subjectCode = entry.getKey();
	        List<Attendance> subjectRecords = entry.getValue();

	        int totalWorkingDays = subjectRecords.size();
	        int totalDaysPresent = (int) subjectRecords.stream().filter(Attendance::getIsPresent).count();
	        int totalDaysAbsent = totalWorkingDays - totalDaysPresent;

	        double attendancePercentage = (totalWorkingDays > 0)
	                ? (totalDaysPresent * 100.0 / totalWorkingDays)
	                : 0.0;

	        GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes.SubjectAttendance sa = new GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes.SubjectAttendance();
	        sa.setSubjectCode(subjectCode);
	        sa.setSubjectShortForm(subjectRecords.get(0).getSemesterSubject().getSubject().getSubjectShortForm()); // Assuming getSubject().getShortForm()
	        sa.setTotalWorkingsDays(totalWorkingDays);
	        sa.setTotalDaysPresent(totalDaysPresent);
	        sa.setTotalDaysAbsent(totalDaysAbsent);
	        sa.setAttendancePercentage(attendancePercentage);

	        subjectAttendances.add(sa);

	        totalOverallPresent += totalDaysPresent;
	        totalOverallWorkingDays += totalWorkingDays;
	    }

	    double semesterAttendance = (totalOverallWorkingDays > 0)
	            ? (totalOverallPresent * 100.0 / totalOverallWorkingDays)
	            : 0.0;

	    GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes res = new GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes();
	    res.setStudentId(studentId);
	    res.setSemesterCode(semesterCode);
	    res.setSemesterAttendance(semesterAttendance);
	    res.setSubjectAttendances(subjectAttendances); // Assuming you have this field and setter

	    return res;
	}
	
	
	@Override
	public GetActiveSemesterAttendanceByStudentIdRes getActiveSemesterAttendanceByStudentId(String studentId) {
	    
	    	Semester activeSemester = semesterRepository.findActiveSemesterByStudentIdAndDate(studentId, LocalDate.now()).orElseThrow();
	    	String semesterCode = activeSemester.getSemesterCode();
	    	
	    	List<Attendance> attendanceList = attendanceRepository
		            .findByStudent_StudentIdAndSemester_SemesterCode(studentId, semesterCode);
	    	Map<String, List<Attendance>> groupedBySubject = attendanceList.stream()
	            .collect(Collectors.groupingBy(a -> a.getSemesterSubject().getSubjectCode()));

	    List<GetActiveSemesterAttendanceByStudentIdRes.SubjectAttendance> subjectAttendances = new ArrayList<>();

	    int totalOverallPresent = 0;
	    int totalOverallWorkingDays = 0;

	    for (Map.Entry<String, List<Attendance>> entry : groupedBySubject.entrySet()) {
	        String subjectCode = entry.getKey();
	        List<Attendance> subjectRecords = entry.getValue();

	        int totalWorkingDays = subjectRecords.size();
	        int totalDaysPresent = (int) subjectRecords.stream().filter(Attendance::getIsPresent).count();
	        int totalDaysAbsent = totalWorkingDays - totalDaysPresent;

	        double attendancePercentage = (totalWorkingDays > 0)
	                ? (totalDaysPresent * 100.0 / totalWorkingDays)
	                : 0.0;

	        GetActiveSemesterAttendanceByStudentIdRes.SubjectAttendance sa = new GetActiveSemesterAttendanceByStudentIdRes.SubjectAttendance();
	        sa.setSubjectCode(subjectCode);
	        sa.setSubjectShortForm(subjectRecords.get(0).getSemesterSubject().getSubject().getSubjectShortForm()); // Assuming getSubject().getShortForm()
	        sa.setTotalWorkingsDays(totalWorkingDays);
	        sa.setTotalDaysPresent(totalDaysPresent);
	        sa.setTotalDaysAbsent(totalDaysAbsent);
	        sa.setAttendancePercentage(attendancePercentage);

	        subjectAttendances.add(sa);

	        totalOverallPresent += totalDaysPresent;
	        totalOverallWorkingDays += totalWorkingDays;
	    }

	    double semesterAttendance = (totalOverallWorkingDays > 0)
	            ? (totalOverallPresent * 100.0 / totalOverallWorkingDays)
	            : 0.0;

	    GetActiveSemesterAttendanceByStudentIdRes res = new GetActiveSemesterAttendanceByStudentIdRes();
	    res.setStudentId(studentId);
	    res.setSemesterCode(semesterCode);
	    res.setSemesterAttendance(semesterAttendance);
	    res.setSubjectAttendances(subjectAttendances); // Assuming you have this field and setter

	    return res;
	}
	
	
	
	@Override
	public List<GetLowAttendanceStudentsByFacultyIdRes> getLowAttendanceStudentsByFacultyId(String facultyId) {
	    log.info("Fetching students with low attendance for faculty ID: {}", facultyId);
	    double attendanceThreshold = 90.0;
	    // Step 1: Fetch all students mentored by this faculty
	    List<Student> students = studentRepository.findAllByFaculty_FacultyId(facultyId);

	    if (students.isEmpty()) {
	        log.info("No students found for faculty ID: {}", facultyId);
	        throw new ResourceNotFoundException(
	            ErrorMessageEnum.FACULTY_ID_NOT_FOUND.getMessage(facultyId)
	        );
	    }

	    // Step 2: Extract student IDs
	    List<String> studentIds = students.stream()
	                                      .map(Student::getStudentId)
	                                      .collect(Collectors.toList());

	    LocalDate today = LocalDate.now();

	    // Step 3: Query attendance repository directly for low attendance students in bulk
	    List<StudentAttendanceSummary> summaries = attendanceRepository.findStudentsWithLowAttendanceByActiveSemester(
	            studentIds, attendanceThreshold, today);

	        // Fallback to latest semester if no active semester records found
	        if (summaries.isEmpty()) {
	            log.info("No active semester attendance records found. Falling back to latest semester.");
	            summaries = attendanceRepository.findStudentsWithLowAttendanceInLatestSemester(
	                studentIds, attendanceThreshold
	            );
	        }
	    // Step 4: Map query results to your DTO
	    List<GetLowAttendanceStudentsByFacultyIdRes> lowAttendanceList = summaries.stream()
	        .map(summary -> {
	            GetLowAttendanceStudentsByFacultyIdRes dto = new GetLowAttendanceStudentsByFacultyIdRes();
	            dto.setStudentId(summary.getStudentId());
	            dto.setSemesterCode(summary.getSemesterCode());
	            dto.setTotalDaysPresent(summary.getTotalDaysPresent());
	            dto.setTotalWorkingDays(summary.getTotalWorkingDays());
	            dto.setAttendancePercentage(summary.getAttendancePercentage());
	            return dto;
	        })
	        .collect(Collectors.toList());

	    return lowAttendanceList;
	}

	
	




}

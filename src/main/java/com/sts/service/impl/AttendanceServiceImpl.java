package com.sts.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.dto.AttendanceUpdateRequest;
import com.sts.entity.Attendance;
import com.sts.entity.Semester;
import com.sts.entity.Student;
import com.sts.exceptions.CustomException;
import com.sts.repository.AttendanceRepository;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.AttendanceRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.AttendanceService;
import com.sts.specification.AttendanceSpecification;
import com.sts.validator.Validator;

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

	

	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, ModelMapper modelMapper,
			StudentRepository studentRepository, FacultyRepository facultyRepository,
			DepartmentRepository departmentRepository, SemesterRepository semesterRepository,
			ValidatorRuleStatus validatorRuleService, ApplicationContext applicationContext) {
		super();
		this.attendanceRepository = attendanceRepository;
		this.modelMapper = modelMapper;
		this.studentRepository = studentRepository;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.semesterRepository = semesterRepository;
		this.validatorRuleService = validatorRuleService;
		this.applicationContext = applicationContext;
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
	public AttendanceResponse saveAttendance(AttendanceRequest attendanceRequest) {
	    log.info("Received a request to save attendance with the following details: {}", attendanceRequest);

	    try {
	        // Check if the validation rule is active
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("The attendance validation rule is active. Let's validate the request.");
	            Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);
	            validator.validate(attendanceRequest);
	            log.info("Validation passed for this attendance request: {}", attendanceRequest);
	        } else {
	            log.warn("Validation rule is not active, skipping the validation step.");
	        }

	        // Mapping the request to an entity and fetching the related student and semester
	        log.debug("Mapping the attendance request to an Attendance entity.");
	        Attendance newAttendance = modelMapper.map(attendanceRequest, Attendance.class);

	        // Fetch the student and semester based on the request
	        log.info("Fetching student with ID: {}", attendanceRequest.getStudentId());
	        Student student = studentRepository.getStudentByStudentId(attendanceRequest.getStudentId());
	        if (student == null) {
	            log.error("Could not find a student with ID: {}", attendanceRequest.getStudentId());
	            throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
	        }
	        newAttendance.setStudent(student);

	        log.info("Fetching semester for the code: {}", attendanceRequest.getSemesterCode());
	        Semester semester = semesterRepository.getSemesterBySemesterCode(attendanceRequest.getSemesterCode());
	        if (semester == null) {
	            log.error("No semester found for the code: {}", attendanceRequest.getSemesterCode());
	            throw new CustomException("Semester not found", HttpStatus.NOT_FOUND);
	        }
	        newAttendance.setSemester(semester);

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

	    } catch (Exception e) {
	        log.error("Something went wrong while saving the attendance record: {}", e.getMessage());
	        throw new CustomException("Failed to save attendance", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
	public String saveMultipleAttendance(List<AttendanceRequest> attendanceRequests) {
	    log.info("Received a request to save multiple attendance records, total records: {}", attendanceRequests.size());

	    try {
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("The validation rule is active, validating each attendance request.");
	            Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);
	            attendanceRequests.forEach(request -> validator.validate(request));
	            log.info("Validation completed for all attendance requests.");
	        } else {
	            log.warn("Validation rule is not active, skipping the validation process.");
	        }

	        // Convert AttendanceRequest list to Attendance entities
	        log.debug("Mapping all attendance requests to Attendance entities.");
	        List<Attendance> attendanceEntities = attendanceRequests.stream()
	            .map(request -> {
	                log.debug("Mapping request for student ID: {}", request.getStudentId());
	                Attendance attendance = modelMapper.map(request, Attendance.class);
	                attendance.setStudent(studentRepository.getStudentByStudentId(request.getStudentId()));
	                attendance.setSemester(semesterRepository.getSemesterBySemesterCode(request.getSemesterCode()));
	                return attendance;
	            })
	            .collect(Collectors.toList());

	        // Save the attendance records
	        log.info("Saving all attendance records in one go...");
	        List<Attendance> savedAttendances = attendanceRepository.saveAll(attendanceEntities);

	        log.info("Successfully saved {} attendance records.", savedAttendances.size());
	        return "Successfully saved " + savedAttendances.size() + " attendance records";

	    } catch (Exception e) {
	        log.error("An error occurred while saving multiple attendance records: {}", e.getMessage());
	        throw new CustomException("Error saving multiple attendance records", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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



}

package com.sts.service.impl;

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
import com.sts.exceptions.CustomException;
import com.sts.repository.AttendanceRepository;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.AttendanceRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.AttendanceService;
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
	public AttendanceResponse saveAttendance(AttendanceRequest attendanceRequest) {
	    log.info("Starting to save attendance with request: {}", attendanceRequest);

	    try {
	        // Validate the request if the validation rule is active
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("Validation rule '{}' is active, starting validation...", ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName());
	            Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);
	            validator.validate(attendanceRequest);  // Validate each attendance request
	            log.info("Validation successful for request: {}", attendanceRequest);
	        } else {
	            log.warn("Validation rule '{}' is not active, skipping validation.", ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName());
	        }

	        // Map the attendance request to attendance entity
	        log.debug("Mapping AttendanceRequest to Attendance entity.");
	        Attendance newAttendance = modelMapper.map(attendanceRequest, Attendance.class);

	        // Dynamically set student and semester based on the request
	        log.info("Fetching student with ID: {}", attendanceRequest.getStudentId());
	        newAttendance.setStudent(studentRepository.getStudentByStudentId(attendanceRequest.getStudentId()));

	        log.info("Fetching semester with code: {}", attendanceRequest.getSemesterCode());
	        newAttendance.setSemester(semesterRepository.getSemesterBySemesterCode(attendanceRequest.getSemesterCode()));

	        // Save the attendance entity to the repository
	        log.info("Saving the attendance entity: {}", newAttendance);
	        Attendance savedAttendance = attendanceRepository.save(newAttendance);
	        log.info("Attendance saved successfully");

	        // Map the saved attendance to the response DTO
	        AttendanceResponse attendanceResponse = modelMapper.map(savedAttendance, AttendanceResponse.class);
	        attendanceResponse.setStudentId(savedAttendance.getStudent().getStudentId());
	        attendanceResponse.setSemesterCode(savedAttendance.getSemester().getSemesterCode());

	        log.info("Returning saved attendance response");
	        return attendanceResponse;  // Return the saved attendance response

	    } catch (Exception e) {
	        // Log the exception message and rethrow as a CustomException
	        log.error("Error saving attendance: {}", e.getMessage());
	        throw new CustomException("Failed to save attendance", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	@Override
	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest) {
	    log.info("Starting to update attendance for student ID: {} and subject: {} on date: {} for period: {}", 
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

	        // Check if the update was successful
	        if (response > 0) {
	            log.info("Successfully updated attendance for student ",attendanceUpdateRequest);
	            
	            return "Successfully updated attendance for student ID: " + attendanceUpdateRequest.getStudentId() 
	                   + " and subject: " + attendanceUpdateRequest.getSubjectCode() 
	                   + " on date: " + attendanceUpdateRequest.getAttendanceDate() 
	                   + " for period: " + attendanceUpdateRequest.getPeriod();
	        } else {
	            // If no records are updated, throw an exception
	            String errorMessage = "No records found to update attendance for student ID: " 
	                                  + attendanceUpdateRequest.getStudentId() 
	                                  + " and subject: " + attendanceUpdateRequest.getSubjectCode()
	                                  + " on date: " + attendanceUpdateRequest.getAttendanceDate()
	                                  + " for period: " + attendanceUpdateRequest.getPeriod();
	            log.error(errorMessage);
	            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);  // Throw custom exception with BAD_REQUEST status
	        }
	    } catch (CustomException e) {
	        // Log the exception message and rethrow the custom exception
	        log.error("Error occurred while updating attendance request for student ID: {}: {}", 
	                  attendanceUpdateRequest.getStudentId(), e.getMessage());
	        throw e;  // Rethrow the custom exception
	    } catch (Exception e) {
	        // Log unexpected errors and throw a custom exception with INTERNAL_SERVER_ERROR status
	        log.error("Unexpected error occurred while updating attendance request for student ID: {}: {}", 
	                  attendanceUpdateRequest.getStudentId(), e.getMessage());
	        throw new CustomException("Unexpected error occurred while updating attendance", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}




	@Override
	public String saveMultipleAttendance(List<AttendanceRequest> attendanceRequests) {
	    log.info("Starting to save multiple attendance records, total records: {}", attendanceRequests.size());

	    try {
	        // Check if attendance request validation rule is active
	        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
	            log.info("Validation rule '{}' is active, starting validation...", ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName());
	            Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);

	            // Validate each attendance request
	            attendanceRequests.forEach(request -> {
	                log.debug("Validating attendance request for student ID: {}", request.getStudentId());
	                validator.validate(request);
	            });
	            log.info("Validation completed for all attendance requests.");
	        } else {
	            log.warn("Validation rule '{}' is not active, skipping validation.", ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName());
	        }

	        // Log the conversion of AttendanceRequest to Attendance entities
	        log.debug("Converting AttendanceRequest list to Attendance entities...");
	        List<Attendance> attendanceEntities = attendanceRequests.stream()
	            .map(request -> {
	                log.debug("Mapping AttendanceRequest for student ID: {} to Attendance entity", request.getStudentId());
	                Attendance attendance = modelMapper.map(request, Attendance.class);
	                attendance.setStudent(studentRepository.getStudentByStudentId(request.getStudentId()));
	                attendance.setSemester(semesterRepository.getSemesterBySemesterCode(request.getSemesterCode()));
	                return attendance;
	            })
	            .collect(Collectors.toList());

	        // Log before saving the attendance records
	        log.info("Saving all attendance records in a single batch...");
	        List<Attendance> savedAttendances = attendanceRepository.saveAll(attendanceEntities);

	        // Log the result of saving attendance records
	        if (savedAttendances.size() == attendanceRequests.size()) {
	            log.info("Successfully saved {} attendance records", savedAttendances.size());
	            return "Successfully saved " + savedAttendances.size() + " attendance records";
	        } else {
	            String errorMessage = "Mismatch in records saved: expected " + attendanceRequests.size() + " but got " + savedAttendances.size();
	            log.error(errorMessage);
	            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);  // Throw custom exception with BAD_REQUEST status
	        }
	    } catch (CustomException e) {
	        // Log the custom exception and rethrow
	        log.error("Error occurred while saving attendance records: {}", e.getMessage());
	        throw e;  // Rethrow the custom exception
	    } catch (Exception e) {
	        // Log unexpected errors and throw a custom exception with INTERNAL_SERVER_ERROR status
	        log.error("Unexpected error occurred while saving attendance records: {}", e.getMessage());
	        throw new CustomException("Unexpected error occurred while saving attendance records", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}





}

package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.dto.AttendanceUpdateRequest;
import com.sts.entity.Attendance;
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
	public AttendanceResponse saveAttedance(AttendanceRequest attendanceRequest) {
		
		log.info("Starting to save attendance with request: {}", attendanceRequest);
		
		// Validate the request
	    if (validatorRuleService.isRuleActive(ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName())) {
	        log.info("Starting {}", ValidatorRulesEnum.ATTENDANCE_REQUEST_VALIDATOR.getRuleName());
	        Validator<AttendanceRequest> validator = applicationContext.getBean(AttendanceRequestValidator.class);
	        validator.validate(attendanceRequest);
	    }
	    
	    Attendance newAttendance = modelMapper.map(attendanceRequest, Attendance.class);
	    
	    newAttendance.setStudent(studentRepository.getStudentByStudentId(attendanceRequest.getStudentId()));
	    newAttendance.setSemester(semesterRepository.getSemesterBySemesterCode(attendanceRequest.getSemesterCode()));
	    
	    Attendance savedAttendance = attendanceRepository.save(newAttendance);
	    
	    AttendanceResponse attendanceResponse = modelMapper.map(savedAttendance, AttendanceResponse.class);
	    attendanceResponse.setStudentId(savedAttendance.getStudent().getStudentId());
	    attendanceResponse.setSemesterCode(savedAttendance.getSemester().getSemesterCode());
		
		
		return attendanceResponse;
	}


	@Override
	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest) {
	    log.info("Starting to update attendance with request: {}", attendanceUpdateRequest);
	    
	    
	    Attendance updatedAttendance = modelMapper.map(attendanceUpdateRequest, Attendance.class);
	    int response;

	    try {
	        // Call the repository method to update the attendance status
	        response = attendanceRepository.updateAttendance(
	                attendanceUpdateRequest.getIsPresent(),
	                attendanceUpdateRequest.getStudentId(),
	                attendanceUpdateRequest.getSubjectCode(),
	                attendanceUpdateRequest.getAttendanceDate(),
	                attendanceUpdateRequest.getPeriod()
	        );

	        // Check if the update was successful
	        if (response > 0) {
	            log.info("Attendance updated successfully");
	            return "Attendance updated successfully";
	        } else {
	            log.warn("No records found to update");
	            return "No records found to update";
	        }
	    } catch (Exception e) {
	        log.error("Error occurred while updating attendance: ", e);
	        return "Failed to update attendance due to an error";
	    }
	}


}

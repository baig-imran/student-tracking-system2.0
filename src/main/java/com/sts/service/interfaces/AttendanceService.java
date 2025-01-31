package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.dto.AttendanceUpdateRequest;

public interface AttendanceService {
	

	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest);
	
	public String saveMultipleAttendance(List<AttendanceRequest> attendanceRequest);

	AttendanceResponse saveAttendance(AttendanceRequest attendanceRequest);

}

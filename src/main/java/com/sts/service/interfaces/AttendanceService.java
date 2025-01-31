package com.sts.service.interfaces;

import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;
import com.sts.dto.AttendanceUpdateRequest;

public interface AttendanceService {
	
	public AttendanceResponse saveAttedance(AttendanceRequest attendanceRequest);

	public String updateAttendance(AttendanceUpdateRequest attendanceUpdateRequest);

}

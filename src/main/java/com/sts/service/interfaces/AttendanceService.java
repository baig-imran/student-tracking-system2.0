package com.sts.service.interfaces;

import com.sts.dto.AttendanceRequest;
import com.sts.dto.AttendanceResponse;

public interface AttendanceService {
	
	public AttendanceResponse saveAttedance(AttendanceRequest attendanceRequest);

}

package com.sts.dto.attendance;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class AddMultiDayStudentAttendance {
    private LocalDate attendanceDate;
    private List<AttendanceRequest> singleDayAttendance;

}

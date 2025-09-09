package com.sts.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceSummary {
    private String studentId;
    private String semesterCode;
    private Long totalDaysPresent;
    private Long totalWorkingDays;
    private Double attendancePercentage;
}
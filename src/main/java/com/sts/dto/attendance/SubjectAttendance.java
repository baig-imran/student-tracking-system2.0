package com.sts.dto.attendance;

import lombok.Data;

@Data
public class SubjectAttendance {
    private String subjectCode;
    private String subjectTitle;
    private int totalWorkingsDays;
    private double attendancePercentage;
    private int totalDaysPresent;
    private int totalDaysAbsent;
}

package com.sts.dto.attendance;

import java.util.List;

import lombok.Data;

@Data
public class GetActiveSemesterLowAttendanceStudentsRes {
    private List<SemesterLowAttendance> semesterWiseResults;

    @Data
    public static class SemesterLowAttendance {
        private String semesterCode;
        private List<LowAttendanceStudent> lowAttendanceStudents;
    }

    @Data
    public static class LowAttendanceStudent {
        private String studentId;
        private String studentName;
        private String semesterCode;
        private double attendancePercentage;
    }
}






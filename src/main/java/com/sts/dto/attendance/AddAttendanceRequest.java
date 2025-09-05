package com.sts.dto.attendance;

import java.util.List;

import lombok.Data;

@Data
public class AddAttendanceRequest {
    private String attendanceDate;
    private String subjectCode;
    private int period;
    private List<StudentStatus> attendanceData;

    @Data
    public static class StudentStatus {
        private Boolean isPresent;
        private String studentId;
    }
}

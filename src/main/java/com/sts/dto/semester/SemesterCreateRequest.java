package com.sts.dto.semester;

import java.time.LocalDate;

public class SemesterCreateRequest {
    private String semesterCode;
    private Integer semesterNumber;
    private Integer studyYear;
    private Integer semesterSerialNumber;
    private Integer academicYear;
    private String regulation;
    private LocalDate startDate;
    private LocalDate endDate;
    private String batch;
    private String departmentId;
}

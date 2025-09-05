package com.sts.dto.exam.studentexam;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Data
public class GetStudentCompleteResultRes {

    private String studentId;
    private String studentName;
    private Double grandTotal;
    private Double cgpa;
    private Double percentage;

    // Semester → List of Subject Summaries
    private Map<Integer, SemesterResult> semesters;

    @Data
    public static class SemesterResult {
        private Double totalInternals;
        private Double totalExternals;
        private Double total;
        private Double sgpa;
        private String academicYear;
        private String examMonth; // e.g., "Apr-21"
        private List<SubjectResultSummary> subjects;
    }

    @Data
    public static class SubjectResultSummary {
        private String subjectCode;
        private String subjectTitle;
        private String subjectShortForm;
        private Double internals;
        private Double externals;
        private Double total;
        private Integer credits;
        private Integer gpa;
        private String passDate;
    }
}

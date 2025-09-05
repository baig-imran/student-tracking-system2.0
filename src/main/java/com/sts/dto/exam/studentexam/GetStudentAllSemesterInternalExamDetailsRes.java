package com.sts.dto.exam.studentexam;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class GetStudentAllSemesterInternalExamDetailsRes {
    private String studentId;
    private Map<Integer, List<SubjectInternalExams>> semesters;

    @Data
    public static class SubjectInternalExams {
        private String subjectCode;
        private String subjectTitle;
        private String subjectShortForm;
        private List<ExamDetail> examDetails; // list of each exam under this subject
        private double total; // sum of marks (only present exams)
    }

    @Data
    @AllArgsConstructor
    public static class ExamDetail {
        private String examName;   // e.g., "Aat1", "Mid1"
        private Double marksObtained; 
        private Boolean isPresent; // true = present, false = absent
    }
}

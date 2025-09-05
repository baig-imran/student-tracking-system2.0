package com.sts.dto.semester;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class SemesterOverallDetailsRes {
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
    private String departmentName;


    private List<FacultyDetails> faculties;
    private List<StudentDetails> students;
    private List<SubjectDetail> subjects;

    @Data
    public static class SubjectDetail {  
        private String subjectCode; // manually given by user
    	private String subjectId;  
    	private String subjectType;
    	private int credits;
    	private String facultyId;
    }
    
    @Data
    public static class FacultyDetails {  

    	private String facultyId;
    	private String facultyName;
    }
    
    @Data
    public static class StudentDetails {  
        private String studentId; 
    	private String studentName;  
    	
    }
}


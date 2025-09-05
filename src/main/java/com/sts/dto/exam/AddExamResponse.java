package com.sts.dto.exam;

import java.time.LocalDate;
import java.util.List;

import com.sts.entity.Semester;
import com.sts.entity.SemesterSubject;
import com.sts.entity.StudentExam;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class AddExamResponse {

    private String examCode; // Common code for all students
    private String examType;
    private String examName;
    private LocalDate examDate;
    private String subjectCode;
    private String semesterCode; // Reference to Semester entity
    private String passMarks;
}

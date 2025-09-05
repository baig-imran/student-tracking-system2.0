package com.sts.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class ExamUpdateRequest {

    @NotNull(message = "Exam name cannot be null")
    private String examName;

    @NotNull(message = "Subject code cannot be null")
    private String subjectCode;

    @NotNull(message = "Student ID cannot be null")
    private String studentId;

    private Double marksObtained;
    private String passMarks;
}

package com.sts.dto.subjects;

import lombok.Data;

@Data
public class SubjectResponse {
    private String subjectId;
    private String subjectTitle;
    private String subjectShortForm;
    private Integer credits;
    private String departmentId;
}

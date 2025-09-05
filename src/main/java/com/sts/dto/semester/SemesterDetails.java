package com.sts.dto.semester;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class SemesterDetails {


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
	    // Referenced by IDs or summary DTOs
	    private List<String> studentIds;
	    private List<String> facultyIds;
	    private List<String> semesterSubjectCodes;

	}

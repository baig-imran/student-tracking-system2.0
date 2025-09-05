package com.sts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subjects {
	
	@Id
    private String subjectId;
	private String subjectTitle;
	private String subjectShortForm;
    private Integer credits; // TODO: remove this, because it is provided in SemesterSubject Entity
    
    @ManyToOne
    @JoinColumn(name="department_id", nullable = false)
    private Department department;
    
    
}

package com.sts.dto.semester;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStartDateAndEndDateBySemesterCodeRes {
	 private LocalDate startDate;
	    private LocalDate endDate;

}

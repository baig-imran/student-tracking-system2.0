package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.constants.SuccessResponse;
import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;
import com.sts.dto.mentoring.GetMentoringStudentsByFacultyIdRes;
import com.sts.service.interfaces.MentoringService;
import com.sts.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoints.V1_MENTORING)
@RequiredArgsConstructor
@Slf4j
public class MentoringController {
	private final MentoringService mentoringService;
	
	@GetMapping("/students/{facultyId}")
	public ResponseEntity<SuccessResponse<List<GetMentoringStudentsByFacultyIdRes>>> getMentoringStudentsByFacultyId(@PathVariable("facultyId") String facultyId) {
		List<GetMentoringStudentsByFacultyIdRes> response = mentoringService.getMentoringStudentsByMentorId(facultyId);
		return ResponseBuilder.ok(response, SuccessMessageEnum.STUDENTS_FETCHED, response.size());
		
	}
	
	
	
}

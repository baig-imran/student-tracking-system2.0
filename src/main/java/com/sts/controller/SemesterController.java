package com.sts.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.SuccessMessageEnum;
import com.sts.constants.SuccessResponse;
import com.sts.dto.semester.AddFacultiesToSemReq;
import com.sts.dto.semester.AddFacultiesToSemRes;
import com.sts.dto.semester.AddStudentsToSemReq;
import com.sts.dto.semester.AddStudentsToSemRes;
import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;
import com.sts.dto.semester.AddSubjectsToSemReq;
import com.sts.dto.semester.AddSubjectsToSemRes;
import com.sts.dto.semester.SemBasicDetailsReq;
import com.sts.dto.semester.SemBasicDetailsRes;
import com.sts.dto.semester.SemesterDetails;
import com.sts.dto.semester.SemesterOverallDetailsReq;
import com.sts.dto.semester.SemesterOverallDetailsRes;
import com.sts.service.interfaces.SemesterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Endpoints.V1_SEMESTERS)
public class SemesterController {
	
	private final SemesterService semesterService;
	
	
	public SemesterController(SemesterService semesterService) {
		super();
		this.semesterService = semesterService;
	}

	@GetMapping("/{semesterCode}")
	public ResponseEntity<?> getSemester(@PathVariable("semesterCode") String semesterCode){
	    log.info("Searching for semester code: {}", semesterCode);
		SemesterDetails semesterResponse = semesterService.getSemester(semesterCode);
		return new ResponseEntity<>(semesterResponse,HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<?> getSemesterOverallDetails(@RequestBody SemesterOverallDetailsReq req){
	    log.info("Searching for semester code: {}", req.getSemesterCode());
		SemesterOverallDetailsRes semesterResponse = semesterService.getOverallSemesterDetails(req.getSemesterCode());
		return new ResponseEntity<>(semesterResponse,HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<SemBasicDetailsRes> createSemester(@RequestBody SemBasicDetailsReq semBasicDetailsReq){
		
		SemBasicDetailsRes semesterResponse = semesterService.saveSemester(semBasicDetailsReq);
		return new ResponseEntity<>(semesterResponse,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/addFaculty")
	public ResponseEntity<SuccessResponse<List<AddFacultiesToSemRes>>> addFaculties(
	        @RequestBody List<AddFacultiesToSemReq> reqList) {
		log.info("Received semester faculty list: {}", reqList);
	    List<AddFacultiesToSemRes> addFacultiesToSemRes = semesterService.addFacultiesToSemester(reqList);

	    SuccessResponse<List<AddFacultiesToSemRes>> response = new SuccessResponse<>(
	        addFacultiesToSemRes,
	        SuccessMessageEnum.FACULTY_ADDED_TO_SEMESTER.getMessage(),
	        HttpStatus.OK.value()
	    );

	    return ResponseEntity.ok(response);
	}

	
	@PostMapping("/addStudents")
	public ResponseEntity<SuccessResponse<List<AddStudentsToSemRes>>> addStudentsToSemester(
	        @RequestBody List<AddStudentsToSemReq> reqList) {
		log.info("Received semester students list: {}", reqList);

	    List<AddStudentsToSemRes> resList = semesterService.addStudentsToSemester(reqList);

	    SuccessResponse<List<AddStudentsToSemRes>> response = new SuccessResponse<>(
	        resList,
	        SuccessMessageEnum.STUDENTS_ADDED_TO_SEMESTER.getMessage(),
	        HttpStatus.OK.value()
	    );

	    return ResponseEntity.ok(response);
	}

	
	@PostMapping("/addSubjects")
	public ResponseEntity<SuccessResponse<List<AddSubjectsToSemRes>>> addSubjects(@RequestBody List<AddSubjectsToSemReq> reqList) {
	    log.info("Received request to map subjects to semesters. Count: {}", reqList.size());
	    List<AddSubjectsToSemRes> resList = semesterService.addSubjectsToSemester(reqList);
	    SuccessResponse<List<AddSubjectsToSemRes>> response = new SuccessResponse<>(resList,
	        SuccessMessageEnum.SUBJECTS_ADDED_TO_SEMESTER.getMessage(), HttpStatus.OK.value());
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/addStudentsToSubject")
	public ResponseEntity<SuccessResponse<List<AddStudentsToSubjectRes>>> addStudentsToSubject(
	        @RequestBody List<AddStudentsToSubjectReq> reqList) {

	    log.info("Received request to add students to subjects. Count: {}", reqList.size());

	    List<AddStudentsToSubjectRes> resList = semesterService.addStudentsToSubject(reqList);

	    SuccessResponse<List<AddStudentsToSubjectRes>> response = new SuccessResponse<>(
	            resList,
	            SuccessMessageEnum.STUDENTS_ADDED_TO_SUBJECT.getMessage(),
	            HttpStatus.OK.value()
	    );

	    return ResponseEntity.ok(response);
	}



}

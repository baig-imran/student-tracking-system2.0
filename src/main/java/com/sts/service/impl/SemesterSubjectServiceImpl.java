package com.sts.service.impl;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.semestersubject.FacultySemesterSubjectStudentsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetRequest;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse.FacultySemesterSubjects;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse2;
import com.sts.dto.semestersubject.FacultySemesterSubjectsGetResponse2.FacultySemesterSubjects2;
import com.sts.entity.SemesterSubject;
import com.sts.entity.StudentSubject;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterSubjectRepository;
import com.sts.service.interfaces.SemesterSubjectService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SemesterSubjectServiceImpl implements SemesterSubjectService {

	private SemesterSubjectRepository semesterSubjectRepository;
    private final FacultyRepository facultyRepository;


	public SemesterSubjectServiceImpl(SemesterSubjectRepository semesterSubjectRepository, FacultyRepository facultyRepository) {
		this.facultyRepository = facultyRepository;
		
		this.semesterSubjectRepository = semesterSubjectRepository;
	}
	
	@Override
	 public List<String> getActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(FacultySemesterSubjectStudentsGetRequest req) {// To get all the students registered for the give subject.
	        LocalDate currentDate = LocalDate.now();
	        
//	        Faculty faculty = facultyRepository.getById(facultyId);
//	        List<SemesterSubject> semesterSubjectList = semesterSubjectRepository.getAllByFaculty(faculty).orElse(null);
//
////	        List<SemesterSubject> semesterSubjectList = semesterSubjectRepository
////	            .findByFacultyAndSemester_StartDateLessThanEqualAndSemester_EndDateGreaterThanEqual(
////	                faculty, currentDate, currentDate);
//	        log.error("{}",semesterSubjectList.size());
//	        
//	        
//	        List<StudentSubject> studentSubjectList =       
//	        semesterSubjectList.stream()
//            .filter(ss -> ss.getSubjectCode().equals(subjectCode))
//            .findFirst()
//            .map(SemesterSubject::getStudents)
//            .orElse(Collections.emptyList());
//	        
//	        log.error("{}",semesterSubjectList.get(0).getStudents().get(0).getStudent().getStudentId());
	        
	        
	        List<StudentSubject> studentSubjectList = semesterSubjectRepository.findActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(
	        		req.getFacultyId(),
	        		req.getSemesterCode(),
	                req.getSubjectCode(),
	                currentDate);
	        
	        List<String> studentIds = new ArrayList<>();
	        studentIds = studentSubjectList.stream()
	        		.map(studentSubject -> 
	        		studentSubject.getStudent().getStudentId()).collect(Collectors.toList());
	        log.info("Student id list {}",studentIds);
	        
	        return studentIds;
	        
	        
	        
	    }

	@Override
	public FacultySemesterSubjectsGetResponse getActiveSemesterSubjectsByFaculty(FacultySemesterSubjectsGetRequest req) {
		
		FacultySemesterSubjectsGetResponse response = new FacultySemesterSubjectsGetResponse();
		
		List<SemesterSubject> semesterSubjects = semesterSubjectRepository.findByFaculty_facultyIdAndSemester_semesterCode(req.getFacultyId(), req.getSemesterCode());
		
		List<FacultySemesterSubjects> facultySemesterSubjects = semesterSubjects.stream().map(semesterSubject -> {
			FacultySemesterSubjects facultySemesterSubject = new FacultySemesterSubjects();
			facultySemesterSubject.setSubjectCode(semesterSubject.getSubjectCode());
			facultySemesterSubject.setSubjectTitle(semesterSubject.getSubject().getSubjectTitle());
			return facultySemesterSubject;
		}).collect(Collectors.toList());
		
		response.setFacultyId(req.getFacultyId());
		response.setSemesterCode(req.getSemesterCode());
		response.setDepartmentId(semesterSubjects.get(0).getSemester().getDepartment().getDepartmentId()); //here we can also get a department w.r.t to subject, but subject may belongs to another department, so choose department id w.r.t Semester
		response.setFacultySemesterSubjects(facultySemesterSubjects);
		
		return response;
	}
	
	@Override
	public FacultySemesterSubjectsGetResponse2 getActiveSemesterSubjectsByFacultyId(String facultyId) {
			
			FacultySemesterSubjectsGetResponse2 response = new FacultySemesterSubjectsGetResponse2();
			
			List<FacultySemesterSubjects2> facultySemesterSubjects = new ArrayList<>();
	        LocalDate currentDate = LocalDate.now();
			List<SemesterSubject> semesterSubjects = semesterSubjectRepository.findActiveSemesterSubjectsByFacultyId(facultyId, currentDate);
			if(semesterSubjects.isEmpty()) {
				throw new  ResourceNotFoundException(ErrorMessageEnum.FACULTY_SEMESTER_SUBJECTS_NOT_FOUND.getMessage());
			}
			
			facultySemesterSubjects = semesterSubjects.stream().map(semesterSubject -> {
				FacultySemesterSubjects2 facultySemesterSubject = new FacultySemesterSubjects2();
				facultySemesterSubject.setSubjectCode(semesterSubject.getSubjectCode());
				facultySemesterSubject.setSubjectTitle(semesterSubject.getSubject().getSubjectTitle());
				facultySemesterSubject.setSemesterCode(semesterSubject.getSemester().getSemesterCode());
				facultySemesterSubject.setDepartmentId(semesterSubject.getSemester().getDepartment().getDepartmentId());
				return facultySemesterSubject;
			}).collect(Collectors.toList());
			
			response.setFacultyId(facultyId);
			response.setFacultySemesterSubjects(facultySemesterSubjects);
			log.info("Faculty Semester Subjects: {}", response);
			return response;
		}
	
	
	
	
	
	
}
	

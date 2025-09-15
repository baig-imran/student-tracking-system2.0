package com.sts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessages;
import com.sts.dto.attendance.GetStudentSemesterAttendanceRes;
import com.sts.dto.mentoring.GetActiveSemesterAttendanceByStudentIdAndSemesterCodeRes;
import com.sts.dto.mentoring.GetMentoringStudentsByFacultyIdRes;
import com.sts.entity.Attendance;
import com.sts.entity.Student;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.AttendanceRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.MentoringService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MentoringServiceImpl implements MentoringService {
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final SemesterRepository semesterRepository;
	private final AttendanceRepository attendanceRepository;


	@Override
	public List<GetMentoringStudentsByFacultyIdRes> getMentoringStudentsByMentorId(String facultyId) {
	    log.info("Fetching mentoring students for faculty ID: {}", facultyId);

	    List<Student> students = studentRepository.findAllByFaculty_FacultyId(facultyId);

	    if (students.isEmpty()) {
	        log.info("No students found for faculty ID: {}", facultyId);
	        throw new ResourceNotFoundException(
	            ErrorMessages.FACULTY_ID_NOT_FOUND.getMessage(facultyId)
	        );
	    }

	    return students.stream()
	        .map(student -> {
	            GetMentoringStudentsByFacultyIdRes dto = modelMapper.map(student, GetMentoringStudentsByFacultyIdRes.class);
	            dto.setDepartmentId(student.getDepartment() != null ? student.getDepartment().getDepartmentId() : null);
	            dto.setMentorId(student.getFaculty() != null ? student.getFaculty().getFacultyId() : null);
	            return dto;
	        })
	        .collect(Collectors.toList());
	}

}

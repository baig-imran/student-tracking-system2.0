package com.sts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessages;
import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;
import com.sts.entity.SemesterSubject;
import com.sts.entity.Student;
import com.sts.entity.StudentSubject;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.SemesterRepository;
import com.sts.repository.SemesterSubjectRepository;
import com.sts.repository.StudentRepository;
import com.sts.repository.StudentSubjectRepository;
import com.sts.service.interfaces.StudentSubjectService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class StudentSubjectServiceImpl implements StudentSubjectService {
	
	private final ModelMapper modelMapper;
	private final StudentSubjectRepository studentSubjectRepository;
	private final SemesterRepository semesterRepository;
	private final StudentRepository studentRepository;
	private final SemesterSubjectRepository semesterSubjectRepository;


	public StudentSubjectServiceImpl(ModelMapper modelMapper, StudentSubjectRepository studentSubjectRepository, SemesterRepository semesterRepository, StudentRepository studentRepository, SemesterSubjectRepository semesterSubjectRepository) {
		super();
		this.modelMapper = modelMapper;
		this.studentSubjectRepository = studentSubjectRepository;
		this.semesterRepository = semesterRepository;
		this.studentRepository = studentRepository;
		this.semesterSubjectRepository = semesterSubjectRepository;
	}

	@Override
	public void saveStudentSubject() {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public List<AddStudentsToSubjectRes> addStudentsToSubject(List<AddStudentsToSubjectReq> reqList) {
	    log.info("Starting to add students to subjects. Total records: {}", reqList.size());

	    if (reqList == null || reqList.isEmpty()) {
	        throw new IllegalArgumentException("Request list cannot be null or empty.");
	    }

	    // Collect unique subject codes and student IDs for bulk fetch
	    Set<String> subjectCodes = reqList.stream()
	                                      .map(AddStudentsToSubjectReq::getSubjectCode)
	                                      .collect(Collectors.toSet());

	    Set<String> studentIds = reqList.stream()
	                                    .map(AddStudentsToSubjectReq::getStudentId)
	                                    .collect(Collectors.toSet());

	    // Bulk fetch SemesterSubjects
	    Map<String, SemesterSubject> semesterSubjectMap = semesterSubjectRepository.findAllById(subjectCodes)
	            .stream()
	            .collect(Collectors.toMap(SemesterSubject::getSubjectCode, Function.identity()));

	    // Bulk fetch Students
	    Map<String, Student> studentMap = studentRepository.findAllById(studentIds)
	            .stream()
	            .collect(Collectors.toMap(Student::getStudentId, Function.identity()));

	    List<AddStudentsToSubjectRes> results = new ArrayList<>();

	    for (AddStudentsToSubjectReq req : reqList) {
	        String subjectCode = req.getSubjectCode();
	        String studentId = req.getStudentId();

	        SemesterSubject semesterSubject = semesterSubjectMap.get(subjectCode);
	        if (semesterSubject == null) {
	            log.error("Subject not found with code: {}", subjectCode);
	            throw new ResourceNotFoundException(ErrorMessages.SUBJECT_ID_NOT_FOUND.getMessage(subjectCode));
	        }

	        Student student = studentMap.get(studentId);
	        if (student == null) {
	            log.error("Student not found with ID: {}", studentId);
	            throw new ResourceNotFoundException("Student not found: " + studentId);
	        }

	        boolean alreadyMapped = semesterSubject.getStudents().stream()
	            .anyMatch(ss -> ss.getStudent().getStudentId().equals(studentId));

	        if (!alreadyMapped) {
	            StudentSubject studentSubject = new StudentSubject();
	            studentSubject.setStudent(student);
	            studentSubject.setSemesterSubject(semesterSubject);
	            studentSubjectRepository.save(studentSubject);

	            log.info("Mapped student {} to subject {}", studentId, subjectCode);

	            AddStudentsToSubjectRes res = new AddStudentsToSubjectRes(subjectCode, studentId);
	            results.add(res);
	        } else {
	            throw new ResourceNotFoundException("Student " + studentId+" already assigned to subject "+subjectCode);
	        }
	    }

	    return results;
	}
	
	@Override
	public List<String> getStudentsBySubjectCode(String subjectCode) {
			
			List<StudentSubject> subjectStudents = studentSubjectRepository.findBySemesterSubject_SubjectCode(subjectCode);

			List<String> subjectStudentIds = subjectStudents.stream().map(studentSubject -> studentSubject.getStudent().getStudentId()).collect(Collectors.toList());
			if(subjectStudentIds.isEmpty()) {
				throw new ResourceNotFoundException("Students not found for the semester code: " + subjectCode);
			}
			return subjectStudentIds;
			}

}

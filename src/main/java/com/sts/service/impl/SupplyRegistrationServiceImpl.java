package com.sts.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.exam.supplyregistration.RegisterSupplyStudentsReq;
import com.sts.entity.Exam;
import com.sts.entity.Student;
import com.sts.entity.SupplyRegistration;
import com.sts.exceptions.DuplicateResourceException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.ExamRepository;
import com.sts.repository.StudentRepository;
import com.sts.repository.StudentSubjectRepository;
import com.sts.repository.SupplyRegistrationRepository;
import com.sts.service.interfaces.SupplyRegistrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class SupplyRegistrationServiceImpl implements SupplyRegistrationService {

	private final SupplyRegistrationRepository registrationRepository;
	private final ExamRepository examRepository;
	private final StudentRepository studentRepository;
	private final StudentSubjectRepository studentSubjectRepository;


	@Override
	public List<String> getSupplyRegisteredStudentIdsByExamCode(String examCode) {
		List<String> res = registrationRepository.findStudentIdsByExamCode(examCode);
		
		if(res.size() == 0) {
			throw new ResourceNotFoundException("No student found for given exam code: " + examCode );
		}
		return res;
	}

	@Override
	public String registerSupplyStudents(RegisterSupplyStudentsReq req) {
		
		String examCode = req.getExamCode(); 
		List<String> studentIds = req.getStudentIds();
	    Exam exam = examRepository.getByExamCode(examCode)
	            .orElseThrow(() -> new ResourceNotFoundException("Exam not found: " + examCode));

	    String subjectCode = exam.getSemesterSubject().getSubjectCode();

	    List<SupplyRegistration> registrationsToSave = studentIds.stream()
	        .map(studentId -> {
	            // 1. Check student-subject mapping
	            if (!studentSubjectRepository.existsBySemesterSubject_SubjectCodeAndStudent_StudentId(subjectCode, studentId)) {
	                throw new ResourceNotFoundException(
	                    ErrorMessageEnum.STUDENT_SUBJECT_MISSMATCH.getMessage(studentId, subjectCode)
	                );
	            }

	            // 2. Get student
	            Student student = studentRepository.findById(studentId)
	                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

	            // 3. Check if already registered
	            if (registrationRepository.existsByExamAndStudent(exam, student)) {
	                throw new DuplicateResourceException(
	                    "Student " + studentId + " already registered for exam: " + examCode
	                );
	            }

	            // 4. Build registration
	            SupplyRegistration reg = new SupplyRegistration();
	            reg.setExam(exam);
	            reg.setStudent(student);
	            reg.setRegisteredDate(LocalDate.now());
	            reg.setType(exam.getExamSubType());
	            reg.setExamCode(examCode);
	            return reg;
	        })
	        .toList();

	    registrationRepository.saveAll(registrationsToSave);
	    return "Successfully registered " + registrationsToSave.size() + " students for exam " + examCode;

	}
	
	@Override
	public String registerBulkSupplyStudents(List<RegisterSupplyStudentsReq> reqList) {
	    log.info("Starting bulk supply registration for {} exams", reqList.size());

	    int totalRegistered = 0;

	    for (RegisterSupplyStudentsReq req : reqList) {
	        String examCode = req.getExamCode();
	        List<String> studentIds = req.getStudentIds();

	        log.info("Registering students for exam: {}", examCode);

	        Exam exam = examRepository.getByExamCode(examCode)
	                .orElseThrow(() -> new ResourceNotFoundException("Exam not found: " + examCode));

	        String subjectCode = exam.getSemesterSubject().getSubjectCode();

	        List<SupplyRegistration> registrationsToSave = studentIds.stream()
	                .map(studentId -> {
	                    // 1. Check student-subject mapping
	                    if (!studentSubjectRepository.existsBySemesterSubject_SubjectCodeAndStudent_StudentId(subjectCode, studentId)) {
	                        throw new ResourceNotFoundException(
	                                ErrorMessageEnum.STUDENT_SUBJECT_MISSMATCH.getMessage(studentId, subjectCode)
	                        );
	                    }

	                    // 2. Get student
	                    Student student = studentRepository.findById(studentId)
	                            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

	                    // 3. Check if already registered
	                    if (registrationRepository.existsByExamAndStudent(exam, student)) {
	                        throw new DuplicateResourceException(
	                                "Student " + studentId + " already registered for exam: " + examCode
	                        );
	                    }

	                    // 4. Build registration
	                    SupplyRegistration reg = new SupplyRegistration();
	                    reg.setExam(exam);
	                    reg.setStudent(student);
	                    reg.setRegisteredDate(LocalDate.now());
	                    reg.setType(exam.getExamSubType());
	                    reg.setExamCode(examCode);
	                    return reg;
	                })
	                .toList();

	        if (!registrationsToSave.isEmpty()) {
	            registrationRepository.saveAll(registrationsToSave);
	            totalRegistered += registrationsToSave.size();
	            log.info("Saved {} supply registrations for exam {}", registrationsToSave.size(), examCode);
	        } else {
	            log.info("No new supply registrations for exam {}", examCode);
	        }
	    }

	    log.info("Bulk supply registration completed. Total students registered: {}", totalRegistered);
	    return "Bulk supply registration completed. Total students registered: " + totalRegistered;
	}



}

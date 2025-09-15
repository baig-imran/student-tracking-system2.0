package com.sts.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessages;
import com.sts.dto.exam.studentexam.AddStudentsExamDataReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataRes;
import com.sts.dto.exam.studentexam.GetInternalMarksByStudentIdAndSemesterCodeRes;
import com.sts.dto.exam.studentexam.GetLowExternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetLowInternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes.SubjectExamSummary;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterInternalExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentCompleteResultRes;
import com.sts.dto.exam.studentexam.GetStudentsWithSupplyByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetSupplyExamDetailsByStudentIdRes;
import com.sts.entity.Exam;
import com.sts.entity.Semester;
import com.sts.entity.SemesterSubject;
import com.sts.entity.Student;
import com.sts.entity.StudentExam;
import com.sts.entity.StudentSubject;
import com.sts.exceptions.DuplicateResourceException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.ExamRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.SemesterSubjectRepository;
import com.sts.repository.StudentExamRepository;
import com.sts.repository.StudentRepository;
import com.sts.repository.StudentSubjectRepository;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.StudentExamService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentExamServiceImpl implements StudentExamService {

	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	public StudentExamServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper,
			FacultyRepository facultyRepository, DepartmentRepository departmentRepository,
			SemesterRepository semesterRepository, ValidatorRuleStatus validatorRuleService,
			ApplicationContext applicationContext, ExamRepository examRepository,
			StudentExamRepository studentExamRepository, SemesterSubjectRepository semesterSubjectRepository,
			StudentSubjectRepository studentSubjectRepository) {
		super();
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
		this.semesterRepository = semesterRepository;
		this.validatorRuleService = validatorRuleService;
		this.applicationContext = applicationContext;
		this.examRepository = examRepository;
		this.studentExamRepository = studentExamRepository;
		this.semesterSubjectRepository = semesterSubjectRepository;
		this.studentSubjectRepository = studentSubjectRepository;
	}

	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final SemesterRepository semesterRepository;
	private final ValidatorRuleStatus validatorRuleService;
	private final ApplicationContext applicationContext;
	private final ExamRepository examRepository;
	private final StudentExamRepository studentExamRepository;
	private final SemesterSubjectRepository semesterSubjectRepository;
	private final StudentSubjectRepository studentSubjectRepository;

//	@Override
//	public AddStudentsExamDataRes addStudentsExamData(AddStudentsExamDataReq req) {
//		log.info("Mapping students exam data to exam: {}", req.getExamCode());
//
//		Exam exam = examRepository.getByExamCode(req.getExamCode())
//				.orElseThrow(() -> new ResourceNotFoundException(
//						ErrorMessages.EXAM_NOT_FOUND.getMessage(req.getExamCode())));
//
//		// checking the existance of first student, since its a bulk student exam data upload then one student's existance proves all others existance        
//		String firstStudentId = req.getStudentsExamData().get(0).getStudentId();
//
//		if (studentExamRepository.existsByStudent_StudentIdAndExamCode(firstStudentId, req.getExamCode())) {
//			throw new DuplicateResourceException(
//					ErrorMessages.DUPLICATE_STUDENT_EXAMDATA.getMessage(req.getExamCode())
//					);
//		}
//
//		List<StudentExam> studentExams = new ArrayList<>();
//		List<String> addedStudentIds = new ArrayList<>();
//
//		for (AddStudentsExamDataReq.StudentExamMap map : req.getStudentsExamData()) {
//			String studentId = map.getStudentId();
//			Double marksObtained = map.getMarksObtained();
//			Boolean isPresent = map.getIsPresent();
//			String examCode = req.getExamCode();
//
//			//Check weather the student registered for the subject or not
//			StudentSubject studentSubject = studentSubjectRepository
//					.getBySubjectCodeAndStudentId(
//							exam.getSemesterSubject().getSubjectCode(), studentId)
//					.orElseThrow(() -> new ResourceNotFoundException(
//							ErrorMessages.STUDENT_SUBJECT_MISSMATCH.getMessage(
//									studentId, exam.getSemesterSubject().getSubjectCode())));
//
//			Student student = studentSubject.getStudent();
//			StudentExam studentExam = new StudentExam();
//			studentExam.setExam(exam);
//			studentExam.setStudent(student);
//			studentExam.setMarksObtained(marksObtained);
//			studentExam.setIsPresent(isPresent);
//			studentExam.setExamCode(examCode);
//
//			studentExams.add(studentExam);
//			addedStudentIds.add(studentId);
//
//			// Check if the student already has an entry for the exam (optional logic)
//			//	            boolean alreadyExists = studentExamRepository.existsByExamAndStudent(exam, student);
//			//	            if (!alreadyExists) {
//			//	                StudentExam studentExam = new StudentExam();
//			//	                studentExam.setExam(exam);
//			//	                studentExam.setStudent(student);
//			//	                studentExam.setMarksObtained(marksObtained);
//			//	                studentExam.setIsPresent(isPresent);
//			//	                studentExam.setExamCode(examCode);
//			//
//			//	                studentExams.add(studentExam);
//			//	                addedStudentIds.add(studentId);
//			//
//			//	                log.debug("Mapped student {} to exam {}", studentId, req.getExamCode());
//			//	            } else {
//			//	                log.warn("Student {} already mapped to exam {}, skipping", studentId, req.getExamCode());
//			//	            }
//		}
//
//		if (!studentExams.isEmpty()) {
//			studentExamRepository.saveAll(studentExams);
//			log.info("Saved {} student exam records for exam {}", studentExams.size(), req.getExamCode());
//		} else {
//			log.info("No new student exam records to save for exam {}", req.getExamCode());
//		}
//
//		AddStudentsExamDataRes res = new AddStudentsExamDataRes();
//		res.setExamCode(exam.getExamCode());
//		res.setStudentIds(addedStudentIds);
//
//		log.info("Students successfully mapped to exam {}", req.getExamCode());
//		return res;
//	}
	
	public AddStudentsExamDataRes addStudentsExamData(AddStudentsExamDataReq req) {
	    log.info("Mapping students exam data to exam: {}", req.getExamCode());

	    Exam exam = examRepository.findByExamCode(req.getExamCode())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    ErrorMessages.EXAM_NOT_FOUND.getMessage(req.getExamCode())));

	    String firstStudentId = req.getStudentsExamData().get(0).getStudentId();
	    if (studentExamRepository.existsByStudent_StudentIdAndExamCode(firstStudentId, req.getExamCode())) {
	        throw new DuplicateResourceException(
	                ErrorMessages.DUPLICATE_STUDENT_EXAMDATA.getMessage(req.getExamCode())
	        );
	    }

	    List<StudentExam> studentExams = new ArrayList<>();
	    List<String> addedStudentIds = new ArrayList<>();

	    for (AddStudentsExamDataReq.StudentExamMap map : req.getStudentsExamData()) {
	        String studentId = map.getStudentId();
	        Double marksObtained = map.getMarksObtained();
	        Boolean isPresent = map.getIsPresent();
	        String examCode = req.getExamCode();

	        // Check if student is registered for subject
	        StudentSubject studentSubject = studentSubjectRepository
	                .getBySubjectCodeAndStudentId(
	                        exam.getSemesterSubject().getSubjectCode(), studentId)
	                .orElseThrow(() -> new ResourceNotFoundException(
	                        ErrorMessages.STUDENT_SUBJECT_MISSMATCH.getMessage(
	                                studentId, exam.getSemesterSubject().getSubjectCode())));

	        Student student = studentSubject.getStudent();

	        StudentExam studentExam = new StudentExam();
	        studentExam.setExam(exam);
	        studentExam.setStudent(student);
	        studentExam.setMarksObtained(marksObtained);
	        studentExam.setIsPresent(isPresent);
	        studentExam.setExamCode(examCode);

	        studentExams.add(studentExam);
	        addedStudentIds.add(studentId);

	        // Supply Exam logic: update marks in previous regular exam if passed
	        if ("supply".equalsIgnoreCase(exam.getExamSubType())
	                && Boolean.TRUE.equals(isPresent)
	                && marksObtained != null
	                && marksObtained >= exam.getPassMarks()) {

	            Optional<StudentExam> regularExamOpt = studentExamRepository
	                    .findLatestByStudentIdAndSubjectCodeAndExamSubType(
	                            studentId,
	                            exam.getSemesterSubject().getSubjectCode(),
	                            "regular" // Or "internal" based on your classification
	                    );

	            if (regularExamOpt.isPresent()) {
	                StudentExam regularExam = regularExamOpt.get();
	                regularExam.setMarksObtained(marksObtained);
	                regularExam.setIsPresent(true); // Optional
	                studentExamRepository.save(regularExam);

	                log.info("Updated regular exam marks for student {} based on supply exam", studentId);
	            } else {
	                log.warn("No regular exam found for student {} to update", studentId);
	            }
	        }
	    }

	    if (!studentExams.isEmpty()) {
	        studentExamRepository.saveAll(studentExams);
	        log.info("Saved {} student exam records for exam {}", studentExams.size(), req.getExamCode());
	    } else {
	        log.info("No new student exam records to save for exam {}", req.getExamCode());
	    }

	    AddStudentsExamDataRes res = new AddStudentsExamDataRes();
	    res.setExamCode(exam.getExamCode());
	    res.setStudentIds(addedStudentIds);

	    log.info("Students successfully mapped to exam {}", req.getExamCode());
	    return res;
	}

	
	@Override
	public String addMultipleSubjectExamDataUpload(List<AddStudentsExamDataReq> reqList) {
	    log.info("Starting bulk upload for {} exams", reqList.size());

	    List<AddStudentsExamDataRes> responses = new ArrayList<>();

	    for (AddStudentsExamDataReq req : reqList) {
	        log.info("Mapping students exam data to exam: {}", req.getExamCode());

	        Exam exam = examRepository.findByExamCode(req.getExamCode())
	                .orElseThrow(() -> new ResourceNotFoundException(
	                        ErrorMessages.EXAM_NOT_FOUND.getMessage(req.getExamCode())));

	        // checking the existence of first student
	        String firstStudentId = req.getStudentsExamData().get(0).getStudentId();
	        if (studentExamRepository.existsByStudent_StudentIdAndExamCode(firstStudentId, req.getExamCode())) {
	            throw new DuplicateResourceException(
	                    ErrorMessages.DUPLICATE_STUDENT_EXAMDATA.getMessage(req.getExamCode())
	            );
	        }

	        List<StudentExam> studentExams = new ArrayList<>();
	        List<String> addedStudentIds = new ArrayList<>();

	        for (AddStudentsExamDataReq.StudentExamMap map : req.getStudentsExamData()) {
	            String studentId = map.getStudentId();
	            Double marksObtained = map.getMarksObtained();
	            Boolean isPresent = map.getIsPresent();
	            String examCode = req.getExamCode();

	            // check whether the student registered for the subject
	            StudentSubject studentSubject = studentSubjectRepository
	                    .getBySubjectCodeAndStudentId(
	                            exam.getSemesterSubject().getSubjectCode(), studentId)
	                    .orElseThrow(() -> new ResourceNotFoundException(
	                            ErrorMessages.STUDENT_SUBJECT_MISSMATCH.getMessage(
	                                    studentId, exam.getSemesterSubject().getSubjectCode())));

	            Student student = studentSubject.getStudent();

	            StudentExam studentExam = new StudentExam();
	            studentExam.setExam(exam);
	            studentExam.setStudent(student);
	            studentExam.setMarksObtained(marksObtained);
	            studentExam.setIsPresent(isPresent);
	            studentExam.setExamCode(examCode);

	            studentExams.add(studentExam);
	            addedStudentIds.add(studentId);
	        }

	        if (!studentExams.isEmpty()) {
	            studentExamRepository.saveAll(studentExams);
	            log.info("Saved {} student exam records for exam {}", studentExams.size(), req.getExamCode());
	        } else {
	            log.info("No new student exam records to save for exam {}", req.getExamCode());
	        }

	        AddStudentsExamDataRes res = new AddStudentsExamDataRes();
	        res.setExamCode(exam.getExamCode());
	        res.setStudentIds(addedStudentIds);

	        log.info("Students successfully mapped to exam {}", req.getExamCode());
	        responses.add(res);
	    }

	    log.info("Bulk upload completed. {} exams processed successfully.", responses.size());
	    return "Bulk upload completed "+ responses.size() + " exams processed successfully.";
	}



	//	    @Override
	public AddStudentsExamDataRes addStudentsExamData2(AddStudentsExamDataReq req) {
		log.info("Mapping students to exam: {}", req.getExamCode());

		Exam exam = examRepository.findByExamCode(req.getExamCode())
				.orElseThrow(() -> new ResourceNotFoundException(
						ErrorMessages.EXAM_NOT_FOUND.getMessage(req.getExamCode())));

		List<StudentExam> studentExams = new ArrayList<>();
		List<String> addedStudentIds = new ArrayList<>();

		for (AddStudentsExamDataReq.StudentExamMap map : req.getStudentsExamData()) {
			String studentId = map.getStudentId();
			Double marksObtained = map.getMarksObtained();
			Boolean isPresent = map.getIsPresent();
			String examCode = req.getExamCode();

			StudentSubject studentSubject = studentSubjectRepository
					.getBySubjectCodeAndStudentId(
							exam.getSemesterSubject().getSubjectCode(), studentId)
					.orElseThrow(() -> new ResourceNotFoundException(
							ErrorMessages.STUDENT_SUBJECT_MISSMATCH.getMessage(
									studentId,exam.getSemesterSubject().getSubjectCode())));

			Student student = studentSubject.getStudent();

			// Check if the student already has an entry for the exam (optional logic)
			//	            boolean alreadyExists = studentExamRepository.existsByExamAndStudent(exam, student);
			//	            if (!alreadyExists) {
			//	                StudentExam studentExam = new StudentExam();
			//	                studentExam.setExam(exam);
			//	                studentExam.setStudent(student);
			//	                studentExam.setMarksObtained(marksObtained);
			//	                studentExam.setIsPresent(isPresent);
			//	                studentExam.setExamCode(examCode);
			//
			//	                studentExams.add(studentExam);
			//	                addedStudentIds.add(studentId);
			//
			//	                log.debug("Mapped student {} to exam {}", studentId, req.getExamCode());
			//	            } else {
			//	                log.warn("Student {} already mapped to exam {}, skipping", studentId, req.getExamCode());
			//	            }
		}

		if (!studentExams.isEmpty()) {
			studentExamRepository.saveAll(studentExams);



			log.info("Saved {} student exam records for exam {}", studentExams.size(), req.getExamCode());
		} else {
			log.info("No new student exam records to save for exam {}", req.getExamCode());
		}

		AddStudentsExamDataRes res = new AddStudentsExamDataRes();
		res.setExamCode(exam.getExamCode());
		res.setStudentIds(addedStudentIds);

		log.info("Students successfully mapped to exam {}", req.getExamCode());
		return res;
	}

	@Override
	// Exam contains SemesterSubject, using the SemesterSubject get the students from StudentSubject.
	public List<String> getStudentsByExamCode(String examCode){

		Exam exam = examRepository.findByExamCode(examCode).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.EXAM_NOT_FOUND.getMessage(examCode)));
		List<StudentSubject> subjectStudents = studentSubjectRepository.findBySemesterSubject_SubjectCode(exam.getSemesterSubject().getSubjectCode());

		List<String> subjectStudentIds = subjectStudents.stream().map(studentSubject -> studentSubject.getStudent().getStudentId()).collect(Collectors.toList());
		if(subjectStudentIds.isEmpty()) {
			throw new ResourceNotFoundException("Students not found for the subject" + exam.getSemesterSubject().getSubjectCode()+ " of exam: "+ examCode);
		}
		return subjectStudentIds;
	}


	@Override
	public GetStudentAllSemesterExamDetailsRes getAllSemestersExamDataByStudentId(String studentId) {

		//1 Fetch all student exam records for the given studentId

		List<StudentExam> exams = studentExamRepository.findByStudent_StudentId(studentId);

		//2 Group exams by:
		// Semester → SubjectCode → ExamType
		// Then, sum marks for each exam type
		Map<Integer, Map<String, Map<String, Double>>> groupedData =
				exams.stream().collect(Collectors.groupingBy(
						// First grouping: by Semester Serial Number
						(Function<StudentExam, Integer>) e -> e.getExam().getSemester().getSemesterSerialNumber(),

						// Second grouping: by Subject Code
						Collectors.groupingBy(
								(Function<StudentExam, String>) e -> e.getExam().getSemesterSubject().getSubjectCode(),

								// Third grouping: by Exam Type (Assignment, Mid, etc.)
								Collectors.groupingBy(
										(Function<StudentExam, String>) e -> e.getExam().getExamType(),

										// Summing marks for each exam type
										Collectors.summingDouble(StudentExam::getMarksObtained)
										)
								)
						));

		// 3 Prepare response DTO
		GetStudentAllSemesterExamDetailsRes response = new GetStudentAllSemesterExamDetailsRes();
		response.setStudentId(studentId);

		// This map will hold Semester → List of Subject summaries
		Map<Integer, List<SubjectExamSummary>> semesterMap = new HashMap<>();

		// 4️ Iterate over each semester group
		for (Map.Entry<Integer, Map<String, Map<String, Double>>> semesterEntry : groupedData.entrySet()) {
			Integer semesterNumber = semesterEntry.getKey(); // Example: 1, 2, 3...

			// List of subject summaries for this semester
			List<SubjectExamSummary> subjectSummaries = new ArrayList<>();

			// 5️ Iterate over each subject in this semester
			for (Map.Entry<String, Map<String, Double>> subjectEntry : semesterEntry.getValue().entrySet()) {
				String subjectCode = subjectEntry.getKey(); // Example: MATH101, CSE201

				// Map of ExamType → Marks (already summed)
				Map<String, Double> examTypeMap = subjectEntry.getValue();

				// Extract marks by type (default 0.0 if not present)
				double assignments = examTypeMap.getOrDefault("aat", 0.0);
				double mids = examTypeMap.getOrDefault("mid", 0.0);

				// 6 Build Subject summary
				SubjectExamSummary summary = new SubjectExamSummary();
				summary.setSubjectCode(subjectCode);
				summary.setAssignments(assignments);
				summary.setMids(mids);
				summary.setTotal(assignments + mids); // total = assignments + mids

				// Add to subject summaries
				subjectSummaries.add(summary);
			}

			// 7️ Add semester data into final map
			semesterMap.put(semesterNumber, subjectSummaries);
		}

		// 8️ Attach semester map to response DTO
		response.setSemesters(semesterMap);

		return response;
	}



	@Override
	public GetStudentAllSemesterInternalExamDetailsRes getAllSemestersInternalExamDataByStudentId(String studentId) {
		
		if(!studentRepository.existsById(studentId)) {
			throw new ResourceNotFoundException(ErrorMessages.STUDENT_ID_NOT_FOUND.getMessage(studentId));
			
		}
		// 1️ Fetch all student exam records for the given studentId
		List<StudentExam> exams = studentExamRepository.findByStudent_StudentId(studentId);

		// 2️ Filter only exams where examType == "Internal"
		List<StudentExam> internalExams = exams.stream()
				.filter(e -> "internal".equalsIgnoreCase(e.getExam().getExamType()))
				.toList();

		// 3️ Group exams by: Semester → SubjectCode
		Map<Integer, Map<String, List<StudentExam>>> groupedData =
				internalExams.stream().collect(Collectors.groupingBy(
						(Function<StudentExam, Integer>) e -> e.getExam().getSemester().getSemesterSerialNumber(),

						Collectors.groupingBy(
								(Function<StudentExam, String>) e -> e.getExam().getSemesterSubject().getSubjectCode()
								)
						));

		// 4️ Prepare response DTO
		GetStudentAllSemesterInternalExamDetailsRes response = new GetStudentAllSemesterInternalExamDetailsRes();
		response.setStudentId(studentId);

		// This map will hold Semester → List of Subjects Exam Details
		Map<Integer, List<GetStudentAllSemesterInternalExamDetailsRes.SubjectInternalExams>> semesterMap = new HashMap<>();

		// 5️ Iterate over each semester group
		for (Map.Entry<Integer, Map<String, List<StudentExam>>> semester : groupedData.entrySet()) {
			Integer semesterNumber = semester.getKey();

			// List of subject exam details for this semester
			List<GetStudentAllSemesterInternalExamDetailsRes.SubjectInternalExams> allSubjectsExamDetails = new ArrayList<>();

			// 6️ Iterate over each subject in this semester
			for (Map.Entry<String, List<StudentExam>> subjectEntry : semester.getValue().entrySet()) {
				String subjectCode = subjectEntry.getKey();
				List<StudentExam> subjectExams = subjectEntry.getValue();
				
				// fetch marks from the current subjects's exams
				Map<String, Double> currentSubjectExamMarks = subjectExams.stream()
		                .collect(Collectors.toMap(
		                        e -> e.getExam().getExamName().toLowerCase(), // examName: "aat1","aat2","mid1","mid2"
		                        e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
		                        Double::sum // merge if duplicate exam names
		                ));	
				log.debug("{} -> {}",subjectEntry.getKey(), currentSubjectExamMarks);
				
				// Collect detailed exam info (name, marks, presence)
				List<GetStudentAllSemesterInternalExamDetailsRes.ExamDetail> examDetails = subjectExams.stream()
						.map(e -> new GetStudentAllSemesterInternalExamDetailsRes.ExamDetail(
								e.getExam().getExamName(),
								e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
										Boolean.TRUE.equals(e.getIsPresent()) // handle nulls safely
								))
						.toList();

				double total = finalInternalMarks(currentSubjectExamMarks);

				// Build Subject summary
				GetStudentAllSemesterInternalExamDetailsRes.SubjectInternalExams singleSubjectExamDetails =
						new GetStudentAllSemesterInternalExamDetailsRes.SubjectInternalExams();

				singleSubjectExamDetails.setSubjectCode(subjectCode);
				singleSubjectExamDetails.setSubjectTitle(subjectExams.get(0).getExam().getSemesterSubject().getSubject().getSubjectTitle());
				singleSubjectExamDetails.setSubjectShortForm(subjectExams.get(0).getExam().getSemesterSubject().getSubject().getSubjectShortForm());
				singleSubjectExamDetails.setExamDetails(examDetails); // detailed exam info with marks + presence
				singleSubjectExamDetails.setTotal(total);

				allSubjectsExamDetails.add(singleSubjectExamDetails);
			}

			semesterMap.put(semesterNumber, allSubjectsExamDetails);
		}

		// 7️ Attach semester map to response DTO
		response.setSemesters(semesterMap);

		return response;
	}


	@Override
	public List<String> getInternalExamQualifiedStudentsBySubjectCode(String subjectCode) {
	    // Fetch only internal exams for a subject
	    List<StudentExam> internalExams = studentExamRepository
	            .findByExam_SemesterSubject_SubjectCodeAndExam_ExamType(subjectCode, "internal");

	    // Group by studentId → student exam details
	    Map<String, List<StudentExam>> groupedByStudent =
	            internalExams.stream()
	                    .collect(Collectors.groupingBy(e -> e.getStudent().getStudentId()));

	    List<String> qualifiedStudents = new ArrayList<>();

	    // Iterate through each student’s exam records
	    for (Map.Entry<String, List<StudentExam>> entry : groupedByStudent.entrySet()) {
	        String studentId = entry.getKey();
	        List<StudentExam> exams = entry.getValue();

	        // Build simplified examName → marksObtained map
	        Map<String, Double> examMarks = exams.stream()
	                .collect(Collectors.toMap(
	                        e -> e.getExam().getExamName().toLowerCase(), // examName: "aat1","aat2","mid1","mid2"
	                        e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
	                        Double::sum // merge if duplicate exam names
	                ));

	        // Calculate final internal marks
	        Double totalMarks = finalInternalMarks(examMarks);

	        // Get pass marks (assume from subject entity → passMarks, hardcoded 15.0 for now)
	        Double passMarks = 15.0;

	        if (totalMarks >= passMarks) {
	            qualifiedStudents.add(studentId);
	        }
	    }

	    log.info("QualifiedStudents: {}", qualifiedStudents);
	    return qualifiedStudents;
	}
	@Override
	public List<String> getInternalExamDisQualifiedStudentsBySubjectCode(String subjectCode) {
	    // Fetch only internal exams for the given subject
	    List<StudentExam> internalExams = studentExamRepository
	            .findByExam_SemesterSubject_SubjectCodeAndExam_ExamType(subjectCode, "internal");

	    // Group by studentId → List of StudentExam records
	    Map<String, List<StudentExam>> groupedByStudent =
	            internalExams.stream()
	                    .collect(Collectors.groupingBy(e -> e.getStudent().getStudentId()));

	    List<String> disqualifiedStudents = new ArrayList<>();

	    // Iterate over each student’s grouped exam records
	    for (Map.Entry<String, List<StudentExam>> entry : groupedByStudent.entrySet()) {
	        String studentId = entry.getKey();
	        List<StudentExam> exams = entry.getValue();

	        // Build a map of examName (lowercase) → marksObtained
	        Map<String, Double> examMarks = exams.stream()
	                .collect(Collectors.toMap(
	                        e -> e.getExam().getExamName().toLowerCase(), // e.g., "aat1", "mid2"
	                        e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
	                        Double::sum // in case of duplicate exam names
	                ));

	        // Compute internal marks using utility logic
	        Double totalMarks = finalInternalMarks(examMarks);

	        // Set pass marks (you can fetch from subject table if needed)
	        Double passMarks = 15.0;

	        // If marks are less than pass marks, student is disqualified
	        if (totalMarks < passMarks) {
	            disqualifiedStudents.add(studentId);
	        }
	    }

	    log.info("DisqualifiedStudents for subject {}: {}", subjectCode, disqualifiedStudents);
	    return disqualifiedStudents;
	}

	private Double finalInternalMarks(Map<String, Double> examMarks) {
	    // Extract safely
	    Double aat1 = examMarks.getOrDefault("aat1", 0.0);
	    Double aat2 = examMarks.getOrDefault("aat2", 0.0);
	    Double mid1 = examMarks.getOrDefault("mid1", 0.0);
	    Double mid2 = examMarks.getOrDefault("mid2", 0.0);

	    log.info("AAT1: {}, AAT2: {}, MID1: {}, MID2: {}", aat1, aat2, mid1, mid2);

	    // AAT average
	    double finalAatMarks = (aat1 + aat2) / 2.0;

	    // MID calculation → (best * 75%) + (worst * 25%), scaled down to 20
	    double bestMid = Math.max(mid1, mid2);
	    double worstMid = Math.min(mid1, mid2);
	    log.debug("bestMid {}, worstMid {}",bestMid, worstMid);
	    double finalMidMarks = 0.0;
	    if (bestMid > 0 && worstMid > 0) {
	        // Both mids available
	        finalMidMarks = (bestMid * 0.75) + (worstMid * 0.25);
	    } else if (bestMid > 0) {
	        // Only one mid present
	        finalMidMarks = bestMid/2.33;
	    } else if (worstMid > 0) {
	        finalMidMarks = worstMid/2.33;
	    }
	    if (finalMidMarks > 20.0) finalMidMarks = 20.0;
	    log.info("finalAatMarks: {}, finalMidMarks: {}", finalAatMarks, finalMidMarks);
	    
	    double finalInternalMarks = finalAatMarks + finalMidMarks;
	    return Math.ceil(finalInternalMarks);
	}
	
	
	
	
	@Override
	public List<String> getSEEFailedStudentsByExamCode(String examCode) {
	    log.info("Fetching SEE failed students for examCode: {}", examCode);

	    // 1. Fetch exam
	    Exam exam = examRepository.findByExamCode(examCode)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    ErrorMessages.EXAM_NOT_FOUND.getMessage(examCode)));

	    // 2. Verify exam type and subtype
	    if (!"external".equalsIgnoreCase(exam.getExamType()) ||
	        !"regular".equalsIgnoreCase(exam.getExamSubType())) {
	        log.warn("Exam {} is not an internal SEE exam.", examCode);
	        throw new ResourceNotFoundException(
	                "Exam " + examCode + " is not an external SEE exam");
	    }

	    // 3. Get pass marks
	    Double passMarks = exam.getPassMarks();

	    // 4. Fetch all student exams for this exam
	    List<StudentExam> studentExams = studentExamRepository.findByExam_ExamCode(examCode);
	    if (studentExams.isEmpty()) {
	        log.warn("No student exam records found for examCode: {}", examCode);
	        throw new ResourceNotFoundException(
	                "No student exam records found for examCode: " + examCode);
	    }

	    // 5. Filter failed students
	    List<String> failedStudentIds = studentExams.stream()
	            .filter(se -> se.getMarksObtained() != null && se.getMarksObtained() < passMarks)
	            .map(se -> se.getStudent().getStudentId())
	            .collect(Collectors.toList());

	    if (failedStudentIds.isEmpty()) {
	        log.warn("No failed students found for examCode {}", examCode);
	        throw new ResourceNotFoundException(
	                "No failed students found for examCode: " + examCode);
	    }

	    log.info("Found {} failed students for examCode {}: {}", 
	             failedStudentIds.size(), examCode, failedStudentIds);

	    return failedStudentIds;
	}


	
	
	
	
	
	
	@Override
	public GetStudentCompleteResultRes getOverallMarksByStudentId(String studentId) {
	    List<StudentExam> exams = studentExamRepository.findByStudent_StudentId(studentId);

	    if (exams.isEmpty()) {
	        throw new ResourceNotFoundException("No exam records found for studentId: " + studentId);
	    }

	    Map<Integer, Map<String, List<StudentExam>>> groupedData =
	            exams.stream().collect(Collectors.groupingBy(
	                    e -> e.getExam().getSemester().getSemesterSerialNumber(),
	                    Collectors.groupingBy(e -> e.getExam().getSemesterSubject().getSubjectCode())
	            ));

	    GetStudentCompleteResultRes response = new GetStudentCompleteResultRes();
	    response.setStudentId(studentId);
	    response.setStudentName(exams.get(0).getStudent().getStudentName());
	    double grandTotal = 0.0;
	    double totalCredits = 0.0;
	    double weightedGradePoints = 0.0;

	    Map<Integer, GetStudentCompleteResultRes.SemesterResult> semesters = new HashMap<>();

	    for (Map.Entry<Integer, Map<String, List<StudentExam>>> semesterEntry : groupedData.entrySet()) {
	        Integer semesterNumber = semesterEntry.getKey();
	        List<GetStudentCompleteResultRes.SubjectResultSummary> subjectSummaries = new ArrayList<>();

	        double semesterInternals = 0.0;
	        double semesterExternals = 0.0;
	        double semesterTotal = 0.0;
	        double semesterCredits = 0.0;
	        double semesterWeightedGradePoints = 0.0;

	        String academicYear = null;
	        String examMonth = null;

	        for (Map.Entry<String, List<StudentExam>> subjectEntry : semesterEntry.getValue().entrySet()) {
	            List<StudentExam> subjectExams = subjectEntry.getValue();
	            Exam anyExam = subjectExams.get(0).getExam();

	            String subjectCode = anyExam.getSemesterSubject().getSubjectCode();
	            String subjectTitle = anyExam.getSemesterSubject().getSubject().getSubjectTitle();
	            String subjectShortForm = anyExam.getSemesterSubject().getSubject().getSubjectShortForm();
	            int credits = anyExam.getSemesterSubject().getSubject().getCredits();

	            // Academic year & month (from first exam date)
	            if (academicYear == null || examMonth == null) {
	                LocalDate examDate = anyExam.getExamDate();
	                if (examDate != null) {
	                    academicYear = examDate.getYear() + "-" + (examDate.getYear() + 1);
	                    examMonth = examDate.getMonth().toString().substring(0, 3) + "-" + String.valueOf(examDate.getYear()).substring(2);
	                }
	            }

	            // Collect exam marks
	            Map<String, Double> currentSubjectExamMarks = subjectExams.stream()
	                    .collect(Collectors.toMap(
	                            e -> e.getExam().getExamName().toLowerCase(),
	                            e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
	                            Double::sum
	                    ));

	            double internals = finalInternalMarks(currentSubjectExamMarks);

	            double externals = subjectExams.stream()
	                    .filter(e -> "external".equalsIgnoreCase(e.getExam().getExamType())
	                            && "regular".equalsIgnoreCase(e.getExam().getExamSubType())
	                            && Boolean.TRUE.equals(e.getIsPresent()))
	                    .mapToDouble(e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0)
	                    .sum();

	            double total = internals + externals;
	            int gpa = calculateGpa(total);

	            semesterInternals += internals;
	            semesterExternals += externals;
	            semesterTotal += total;
	            semesterWeightedGradePoints += gpa * credits;
	            semesterCredits += credits;

	            weightedGradePoints += gpa * credits;
	            totalCredits += credits;
	            grandTotal += total;

	            GetStudentCompleteResultRes.SubjectResultSummary subjectSummary = new GetStudentCompleteResultRes.SubjectResultSummary();
	            subjectSummary.setSubjectCode(subjectCode);
	            subjectSummary.setSubjectTitle(subjectTitle);
	            subjectSummary.setSubjectShortForm(subjectShortForm);
	            subjectSummary.setInternals(internals);
	            subjectSummary.setExternals(externals);
	            subjectSummary.setTotal(total);
	            subjectSummary.setCredits(credits);
	            subjectSummary.setGpa(gpa);
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
	            subjectSummary.setPassDate(LocalDate.now().format(formatter).toUpperCase());

	            subjectSummaries.add(subjectSummary);
	        }

	        // Semester SGPA
	        double sgpa = semesterCredits > 0 ? semesterWeightedGradePoints / semesterCredits : 0.0;

	        GetStudentCompleteResultRes.SemesterResult semResult = new GetStudentCompleteResultRes.SemesterResult();
	        semResult.setTotalInternals(semesterInternals);
	        semResult.setTotalExternals(semesterExternals);
	        semResult.setTotal(semesterTotal);
	        semResult.setSgpa(sgpa);
	        semResult.setAcademicYear(academicYear);
	        semResult.setExamMonth(examMonth);
	        semResult.setSubjects(subjectSummaries);

	        semesters.put(semesterNumber, semResult);
	    }

	    // Final CGPA & percentage
	    double cgpa = totalCredits > 0 ? weightedGradePoints / totalCredits : 0.0;
	    double percentage = (grandTotal / (totalCredits * 10)) * 100;

	    response.setGrandTotal(grandTotal);
	    response.setCgpa(cgpa);
	    response.setPercentage(percentage);
	    response.setSemesters(semesters);

	    return response;
	}


	// GPA mapping logic
	private int calculateGpa(double marks) {
	    if (marks >= 91) return 10;
	    if (marks >= 81) return 9;
	    if (marks >= 71) return 8;
	    if (marks >= 61) return 7;
	    if (marks >= 51) return 6;
	    if (marks >= 41) return 5;
	    return 0; // fail
	}

    private static final double PASS_MARKS = 15.0;
    
    @Override
    public List<GetLowInternalMarksStudentsByFacultyIdRes> getLowInternalMarksStudentsByFacultyId(String facultyId) {
        log.info("Fetching low internal marks students for facultyId: {}", facultyId);

        List<Student> students = studentRepository.findAllByFaculty_FacultyId(facultyId);
        if (students == null || students.isEmpty()) {
            log.warn("No students found for facultyId: {}", facultyId);
            return Collections.emptyList();
        }

        List<String> studentIds = students.stream().map(Student::getStudentId).collect(Collectors.toList());
        LocalDate today = LocalDate.now();

        // Try active semester first
        Semester semester = semesterRepository
                .findActiveSemesterByStudentIdAndDate(studentIds.get(0), today)
                .orElseGet(() -> {
                    log.info("No active semester found for studentId: {}, falling back to latest semester", studentIds.get(0));
                    return semesterRepository.findLatestSemesterByStudentId(studentIds.get(0)).orElse(null);
                });

        if (semester == null) {
            log.warn("No active or latest semester found for studentId: {}", studentIds.get(0));
            return Collections.emptyList();
        }

        log.info("Using semester: {} for evaluation", semester.getSemesterCode());

        List<StudentExam> internalExams = studentExamRepository
                .findByStudent_StudentIdInAndExam_ExamTypeAndExam_Semester_SemesterCode(
                        studentIds, "internal", semester.getSemesterCode());

        log.info("Fetched {} internal exam records for semester: {}", internalExams.size(), semester.getSemesterCode());

        // Group exams: studentId -> subjectCode -> List<StudentExam>
        Map<String, Map<String, List<StudentExam>>> studentSubjectExams = internalExams.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getStudent().getStudentId(),
                        Collectors.groupingBy(e -> e.getExam().getSemesterSubject().getSubjectCode())
                ));

        List<GetLowInternalMarksStudentsByFacultyIdRes> lowInternalStudents = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<StudentExam>>> studentEntry : studentSubjectExams.entrySet()) {
            String studentId = studentEntry.getKey();
            Student student = students.stream()
                    .filter(s -> s.getStudentId().equals(studentId))
                    .findFirst()
                    .orElse(null);

            if (student == null) {
                log.warn("Student record not found for ID: {}", studentId);
                continue;
            }

            boolean hasLowSubject = false;
            double totalInternals = 0.0;
            double totalExternals = 0.0;
            double totalCredits = 0.0;
            double weightedGradePoints = 0.0;

            for (Map.Entry<String, List<StudentExam>> subjectEntry : studentEntry.getValue().entrySet()) {
                String subjectCode = subjectEntry.getKey();
                List<StudentExam> subjectExams = subjectEntry.getValue();

                Map<String, Double> examMarks = subjectExams.stream()
                        .collect(Collectors.toMap(
                                e -> e.getExam().getExamName().toLowerCase(),
                                e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
                                Double::sum
                        ));

                double internals = calculateFinalInternalMarks(examMarks);
                double externals = subjectExams.stream()
                        .filter(e -> "external".equalsIgnoreCase(e.getExam().getExamType())
                                && "regular".equalsIgnoreCase(e.getExam().getExamSubType())
                                && Boolean.TRUE.equals(e.getIsPresent()))
                        .mapToDouble(e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0)
                        .sum();

                double subjectTotal = internals + externals;
                int subjectCredits = subjectExams.get(0).getExam().getSemesterSubject().getSubject().getCredits();
                int gpa = calculateGpa(subjectTotal);

                totalInternals += internals;
                totalExternals += externals;
                totalCredits += subjectCredits;
                weightedGradePoints += gpa * subjectCredits;

                log.debug("Student: {}, Subject: {}, Internals: {}, Externals: {}, Total: {}",
                        studentId, subjectCode, internals, externals, subjectTotal);

                if (internals < PASS_MARKS) {
                    hasLowSubject = true;
                    log.info("Student {} has low internals ({}) in subject {}", studentId, internals, subjectCode);
                }
            }

            if (hasLowSubject) {
                GetLowInternalMarksStudentsByFacultyIdRes res = new GetLowInternalMarksStudentsByFacultyIdRes();
                res.setStudentId(studentId);
                res.setStudentName(student.getStudentName());
                res.setSemesterCode(semester.getSemesterCode());
                res.setTotalInternals(totalInternals);
                res.setTotalExternals(totalExternals);
                res.setTotal(totalInternals + totalExternals);
                res.setSgpa(totalCredits > 0 ? weightedGradePoints / totalCredits : 0.0);
                res.setAcademicYear(String.valueOf(semester.getAcademicYear()));

                log.info("Student {} added to low internal list", studentId);
                lowInternalStudents.add(res);
            }
        }

        log.info("Total students with low internal marks: {}", lowInternalStudents.size());
        return lowInternalStudents;
    }

	private double calculateFinalInternalMarks(Map<String, Double> examMarks) {
        double aat1 = examMarks.getOrDefault("aat1", 0.0);
        double aat2 = examMarks.getOrDefault("aat2", 0.0);
        double mid1 = examMarks.getOrDefault("mid1", 0.0);
        double mid2 = examMarks.getOrDefault("mid2", 0.0);

        double finalAat = (aat1 + aat2) / 2.0;

        double bestMid = Math.max(mid1, mid2);
        double worstMid = Math.min(mid1, mid2);

        double finalMid = 0.0;
        if (bestMid > 0 && worstMid > 0) {
            finalMid = (bestMid * 0.75) + (worstMid * 0.25);
        } else if (bestMid > 0) {
            finalMid = bestMid / 2.33;
        } else if (worstMid > 0) {
            finalMid = worstMid / 2.33;
        }

        finalMid = Math.min(finalMid, 20.0);
        return Math.ceil(finalAat + finalMid);
    }
	
	@Override
	public List<GetInternalMarksByStudentIdAndSemesterCodeRes> getInternalMarksByStudentIdAndSemesterCode(String studentId, String semesterCode) {
	    List<StudentExam> exams = studentExamRepository
	            .findByStudent_StudentIdAndExam_Semester_SemesterCode(studentId, semesterCode);

	    if (exams.isEmpty()) {
	        throw new ResourceNotFoundException("No exam data found for studentId: " + studentId + " and semester: " + semesterCode);
	    }

	    Map<String, List<StudentExam>> groupedBySubject = exams.stream()
	            .collect(Collectors.groupingBy(e -> e.getExam().getSemesterSubject().getSubjectCode()));

	    List<GetInternalMarksByStudentIdAndSemesterCodeRes> results = new ArrayList<>();

	    for (Map.Entry<String, List<StudentExam>> entry : groupedBySubject.entrySet()) {
	        List<StudentExam> subjectExams = entry.getValue();
	        Exam exam = subjectExams.get(0).getExam();

	        Map<String, Double> examMarks = subjectExams.stream()
	                .collect(Collectors.toMap(
	                        e -> e.getExam().getExamName().toLowerCase(),
	                        e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0,
	                        Double::sum
	                ));

	        double internals = calculateFinalInternalMarks(examMarks);
	        double externals = subjectExams.stream()
	                .filter(e -> "external".equalsIgnoreCase(e.getExam().getExamType())
	                        && "regular".equalsIgnoreCase(e.getExam().getExamSubType())
	                        && Boolean.TRUE.equals(e.getIsPresent()))
	                .mapToDouble(e -> e.getMarksObtained() != null ? e.getMarksObtained() : 0.0)
	                .sum();

	        double total = internals + externals;
	        int credits = exam.getSemesterSubject().getSubject().getCredits();
	        int gpa = calculateGpa(total);

	        GetInternalMarksByStudentIdAndSemesterCodeRes dto = new GetInternalMarksByStudentIdAndSemesterCodeRes();
	        dto.setSubjectCode(exam.getSemesterSubject().getSubjectCode());
	        dto.setSubjectTitle(exam.getSemesterSubject().getSubject().getSubjectTitle());
	        dto.setSubjectShortForm(exam.getSemesterSubject().getSubject().getSubjectShortForm());
	        dto.setInternals(internals);
	        dto.setExternals(externals);
	        dto.setTotal(total);
	        dto.setCredits(credits);
	        dto.setGpa(gpa);
	        dto.setPassDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH)).toUpperCase());

	        results.add(dto);
	    }

	    return results;
	}
	
	
	@Override
	public List<GetLowExternalMarksStudentsByFacultyIdRes> getLowExternalMarksStudentsByFacultyId(String facultyId) {
	    log.info("Fetching students with low external marks for facultyId: {}", facultyId);
	    double EXTERNAL_PASS_MARKS = 25.0;
	    List<Student> students = studentRepository.findAllByFaculty_FacultyId(facultyId);
	    if (students == null || students.isEmpty()) {
	        log.warn("No students found for facultyId: {}", facultyId);
	        return Collections.emptyList();
	    }

	    List<String> studentIds = students.stream()
	            .map(Student::getStudentId)
	            .collect(Collectors.toList());

	    LocalDate today = LocalDate.now();

	    // Use first student to get active or latest semester (assuming same semester for all)
	    Semester semester = semesterRepository
	            .findActiveSemesterByStudentIdAndDate(studentIds.get(0), today)
	            .orElseGet(() -> semesterRepository.findLatestSemesterByStudentId(studentIds.get(0)).orElse(null));

	    if (semester == null) {
	        log.warn("No semester found for student: {}", studentIds.get(0));
	        return Collections.emptyList();
	    }

	    // Fetch only external exams for these students in the selected semester
	    List<StudentExam> externalExams = studentExamRepository
	            .findByStudent_StudentIdInAndExam_ExamTypeAndExam_Semester_SemesterCode(
	                    studentIds, "external", semester.getSemesterCode());

	    log.info("Found {} external exam records for semester {}", externalExams.size(), semester.getSemesterCode());

	    List<GetLowExternalMarksStudentsByFacultyIdRes> response = new ArrayList<>();

	    for (StudentExam exam : externalExams) {
//	        if (!Boolean.TRUE.equals(exam.getIsPresent())) continue; // Skip absentees

	        Double marks = exam.getMarksObtained() != null ? exam.getMarksObtained() : 0.0;

	        if (marks < EXTERNAL_PASS_MARKS) {
	            Student student = exam.getStudent();
	            SemesterSubject subject = exam.getExam().getSemesterSubject();

	            GetLowExternalMarksStudentsByFacultyIdRes dto = new GetLowExternalMarksStudentsByFacultyIdRes();
	            dto.setStudentId(student.getStudentId());
	            dto.setStudentName(student.getStudentName());
	            dto.setSubjectCode(subject.getSubjectCode());
	            dto.setSubjectTitle(subject.getSubject().getSubjectTitle());
	            dto.setSubjectShortForm(subject.getSubject().getSubjectShortForm());
	            dto.setExamCode(exam.getExamCode());
	            dto.setMarksObtained(marks);
	            dto.setPassMarks(EXTERNAL_PASS_MARKS);
	            //dto.setPassDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-yy")).toUpperCase());

	            response.add(dto);
	        }
	    }

	    log.info("Found {} students with low external marks", response.size());

	    return response;
	}
	
	@Override
	public List<GetStudentsWithSupplyByFacultyIdRes> getStudentsWithSupplyByFacultyId(String facultyId) {
	    log.info("Fetching students with supply (failed external subjects across all semesters) for facultyId: {}", facultyId);
	    double EXTERNAL_PASS_MARKS = 25.0;

	    List<Student> students = studentRepository.findAllByFaculty_FacultyId(facultyId);
	    if (students == null || students.isEmpty()) {
	        log.warn("No students found for facultyId: {}", facultyId);
	        return Collections.emptyList();
	    }

	    List<String> studentIds = students.stream()
	            .map(Student::getStudentId)
	            .collect(Collectors.toList());

	    // Fetch all external exam records across all semesters
	    List<StudentExam> externalExams = studentExamRepository
	            .findByStudent_StudentIdInAndExam_ExamType(studentIds, "external");

	    log.info("Found {} external exam records across all semesters", externalExams.size());

	    // Group by student
	    Map<String, List<StudentExam>> examsByStudent = externalExams.stream()
	            .collect(Collectors.groupingBy(exam -> exam.getStudent().getStudentId()));

	    List<GetStudentsWithSupplyByFacultyIdRes> response = new ArrayList<>();

	    for (Map.Entry<String, List<StudentExam>> entry : examsByStudent.entrySet()) {
	        String studentId = entry.getKey();
	        List<StudentExam> studentExams = entry.getValue();

	        long supplyCount = studentExams.stream()
	                // .filter(exam -> Boolean.TRUE.equals(exam.getIsPresent())) // uncomment if needed
	                .filter(exam -> {
	                    Double marks = exam.getMarksObtained() != null ? exam.getMarksObtained() : 0.0;
	                    return marks < EXTERNAL_PASS_MARKS;
	                })
	                .count();

	        if (supplyCount > 0) {
	            Student student = studentExams.get(0).getStudent(); // All exams belong to same student
	            GetStudentsWithSupplyByFacultyIdRes dto = new GetStudentsWithSupplyByFacultyIdRes();
	            dto.setStudentId(studentId);
	            dto.setStudentName(student.getStudentName());
	            dto.setSupplyCount((int) supplyCount);
	            response.add(dto);
	        }
	    }

	    log.info("Found {} students with supply subjects (all semesters)", response.size());
	    return response;
	}
	
	@Override
	public List<GetSupplyExamDetailsByStudentIdRes> getSupplyExamDetailsByStudentId(String studentId) {
	    log.info("Fetching supply exam details for studentId: {}", studentId);
	    final double EXTERNAL_PASS_MARKS = 25.0;

	    Student student = studentRepository.findById(studentId)
		        .orElseThrow(() -> new ResourceNotFoundException(
		            ErrorMessages.STUDENT_ID_NOT_FOUND.getMessage(studentId)));

	    // Fetch all external exams (across all semesters) for the student
	    List<StudentExam> externalExams = studentExamRepository
	            .findByStudent_StudentIdAndExam_ExamType(studentId, "external");

	    if (externalExams.isEmpty()) {
	        log.info("No external exams found for studentId: {}", studentId);
	        return Collections.emptyList();
	    }

	    List<GetSupplyExamDetailsByStudentIdRes> supplyExams = new ArrayList<>();

	    for (StudentExam exam : externalExams) {
	        Double marks = exam.getMarksObtained() != null ? exam.getMarksObtained() : 0.0;
	        if (marks < EXTERNAL_PASS_MARKS) {
	            SemesterSubject subject = exam.getExam().getSemesterSubject();

	            GetSupplyExamDetailsByStudentIdRes dto = new GetSupplyExamDetailsByStudentIdRes();
	            dto.setStudentId(student.getStudentId());
	            dto.setStudentName(student.getStudentName());
	            dto.setSubjectCode(subject.getSubjectCode());
	            dto.setSubjectTitle(subject.getSubject().getSubjectTitle());
	            dto.setSubjectShortForm(subject.getSubject().getSubjectShortForm());
	            dto.setExamCode(exam.getExamCode());
	            dto.setMarksObtained(marks);
	            dto.setPassMarks(EXTERNAL_PASS_MARKS);

	            supplyExams.add(dto);
	        }
	    }

	    log.info("Found {} supply subjects for studentId: {}", supplyExams.size(), studentId);
	    return supplyExams;
	}







}
	
	


//package com.sts.service.impl;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;
//
//import com.sts.constants.ErrorMessageEnum;
//import com.sts.constants.SuccessMessageEnum;
//import com.sts.constants.ValidatorRulesEnum;
//import com.sts.dto.AddExamRequest;
//import com.sts.dto.AddExamResponse;
//import com.sts.dto.ExamUpdateRequest;
//import com.sts.entity.Exam;
//import com.sts.entity.StudentExam;
//import com.sts.exceptions.DatabaseException;
//import com.sts.exceptions.FilterCriteriaException;
//import com.sts.repository.DepartmentRepository;
//import com.sts.repository.ExamRepository;
//import com.sts.repository.FacultyRepository;
//import com.sts.repository.SemesterRepository;
//import com.sts.repository.StudentExamRepository;
//import com.sts.repository.StudentRepository;
//import com.sts.service.impl.validators.ExamRequestValidator;
//import com.sts.service.impl.validators.ValidatorRuleStatus;
//import com.sts.service.interfaces.ExamService;
//import com.sts.specification.StudentExamSpecification;
//import com.sts.validator.Validator;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class ExamServiceImpl implements ExamService {
//
//    private final StudentRepository studentRepository;
//    private final ModelMapper modelMapper;
//    private final FacultyRepository facultyRepository;
//    private final DepartmentRepository departmentRepository;
//    private final SemesterRepository semesterRepository;
//    private final ValidatorRuleStatus validatorRuleService;
//    private final ApplicationContext applicationContext;
//    private final ExamRepository examRepository;
//    private final StudentExamRepository studentExamRepository;
//
//    public ExamServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, SemesterRepository semesterRepository, ValidatorRuleStatus validatorRuleService, ApplicationContext applicationContext, ExamRepository examRepository, StudentExamRepository studentExamRepository) {
//        this.studentRepository = studentRepository;
//        this.modelMapper = modelMapper;
//        this.facultyRepository = facultyRepository;
//        this.departmentRepository = departmentRepository;
//        this.semesterRepository = semesterRepository;
//        this.validatorRuleService = validatorRuleService;
//        this.applicationContext = applicationContext;
//        this.examRepository = examRepository;
//		this.studentExamRepository = studentExamRepository;
//    }
//
//    @Override
//    public List<AddExamResponse> getExams(AddExamRequest filterRequest) {
//        log.info("Fetching exam records based on filter criteria: {}", filterRequest);
//        if (isFilterEmpty(filterRequest)) {
//        	throw new FilterCriteriaException();
//        }
//
//        try {
//            List<StudentExam> examList = studentExamRepository.findAll(StudentExamSpecification.getExamSpec(filterRequest));
//
//            if (examList.isEmpty()) {
//                log.warn("No exam records found for the given filter criteria.");
//                return Collections.emptyList();
//            }
//
//            List<AddExamResponse> responseList = examList.stream()
//                    .map(exam -> {
//                        AddExamResponse response = modelMapper.map(exam, AddExamResponse.class);
//                        response.setStudentId(exam.getStudent().getStudentId());
//                        response.setSemesterCode(exam.getExam().getSemesterSubject().getSemester().getSemesterCode());
//                        return response;
//                    })
//                    .collect(Collectors.toList());
//
//            log.info("Successfully fetched {} exam records.", responseList.size());
//            return responseList;
//        } catch (Exception ex) {
//            log.error("Error occurred while fetching exams: {}", ex.getMessage());
//            throw new DatabaseException(ErrorMessageEnum.FAILED_TO_FETCH_EXAMS.getMessage());
//        }
//    }
//
//    @Override
//    public AddExamResponse saveExam(AddExamRequest examRequest) {
//    	
//    	if (isFilterEmpty(examRequest)) {
//            throw new FilterCriteriaException();
//        }
//        log.info("Saving new exam: {}", examRequest);
//
//        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName())) {
//            Validator<AddExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);
//            validator.validate(examRequest);
//        }
//
//        try {
//            Exam newExam = modelMapper.map(examRequest, Exam.class);
//            newExam. .getExam().getSemesterSubject().setSemester(semesterRepository.getSemesterBySemesterCode(examRequest.getSemesterCode()));
//            newExam.setStudent(studentRepository.getStudentByStudentId(examRequest.getStudentId()));
//
//            Exam savedExam = examRepository.save(newExam);
//            log.info("Exam saved successfully with ID: {}", savedExam.getExamCode());
//
//            AddExamResponse examResponse = modelMapper.map(savedExam, AddExamResponse.class);
//            examResponse.setSemesterCode(savedExam.getSemester().getSemesterCode());
//            examResponse.setStudentId(savedExam.getStudent().getStudentId());
//
//            return examResponse;
//        } catch (Exception ex) {
//            log.error("Error occurred while saving exam: {}", ex.getMessage(), ex);
//            throw new DatabaseException(ErrorMessageEnum.FAILED_TO_SAVE_EXAM.getMessage());
//        }
//    }
//
//    @Override
//    public String updateExam(ExamUpdateRequest examUpdateRequest) {
//    	
//
//        log.info("Updating exam for student ID: {}, subject: {} and exam {}", 
//                 examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode(), examUpdateRequest.getExamName());
//
//        try {
//            int updated = examRepository.updateExam(
//                    examUpdateRequest.getStudentId(),
//                    examUpdateRequest.getSubjectCode(),
//                    examUpdateRequest.getExamName(),
//                    examUpdateRequest.getMarksObtained()
//            );
//
//            if (updated > 0) {
//                log.info("Successfully updated exam.");
//                return SuccessMessageEnum.EXAM_UPDATED_SUCCESSFULLY.getMessage();
//            } else {
//                
//                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_UPDATE_EXAM.getMessage());
//            }
//        } catch (DatabaseException ex) {
//            throw ex; 
//        } catch (Exception ex) {
//            log.error("Error occurred while updating exam");
//            throw ex;
//        }
//    }
//
//    @Override
//    public String saveMultipleExams(List<AddExamRequest> examRequests) {
//        log.info("Saving multiple exams, count: {}", examRequests.size());
//
//        try {
//            List<Exam> exams = examRequests.stream()
//                    .map(req -> {
//                        Exam exam = modelMapper.map(req, Exam.class);
//                        exam.setStudent(studentRepository.getStudentByStudentId(req.getStudentId()));
//                        exam.setSemester(semesterRepository.getSemesterBySemesterCode(req.getSemesterCode()));
//                        return exam;
//                    })
//                    .collect(Collectors.toList());
//
//            List<Exam> savedExams = examRepository.saveAll(exams);
//
//            if (savedExams.size() != examRequests.size()) {
//                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_SAVE_EXAM.getMessage());
//            }
//
//            return SuccessMessageEnum.EXAMS_SAVED_SUCCESSFULLY.getMessage();
//        }catch (DatabaseException ex) {
//            throw ex; 
//            
//        }catch (Exception ex) {
//            log.error("Error occurred while saving multiple exams");
//            throw ex;
//        }
//    }
//
//    @Override
//    public String updateMultipleExams(List<ExamUpdateRequest> examUpdateRequests) {
//        log.info("Updating multiple exams, count: {}", examUpdateRequests.size());
//
//        try {
//            int totalUpdated = 0;
//            for (ExamUpdateRequest request : examUpdateRequests) {
//                int updated = examRepository.updateExam(
//                        request.getStudentId(),
//                        request.getSubjectCode(),
//                        request.getExamName(),
//                        request.getMarksObtained()
//                );
//                if (updated > 0) {
//                    totalUpdated++;
//                }
//            }
//
//            if (totalUpdated == examUpdateRequests.size()) {
//                log.info("Successfully updated all {} exams.", examUpdateRequests.size());
//                return SuccessMessageEnum.EXAMS_UPDATED_SUCCESSFULLY.getMessage();
//            } else {
//                log.warn("Failed to update {} exams.", examUpdateRequests.size() - totalUpdated);
//                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_UPDATE_EXAM.getMessage());
//            }
//        }catch (DatabaseException ex) {
//            throw ex; 
//        }
//        catch (Exception ex) {
//            log.error("Error occurred while updating multiple exams");
//            throw ex;
//        }
//    }
//
//    private boolean isFilterEmpty(AddExamRequest filterRequest) {
//        return (filterRequest.getExamCode() == null 
//                && filterRequest.getExamType() == null 
//                && filterRequest.getExamName() == null 
//                && filterRequest.getExamDate() == null 
//                && filterRequest.getSubjectCode() == null 
//                && filterRequest.getMarksObtained() == null 
//                && filterRequest.getSemesterCode() == null 
//                && filterRequest.getStudentId() == null);
//    }
//    
//    
//    
//}

package com.sts.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.dto.exam.AddExamRequest;
import com.sts.dto.exam.AddExamResponse;
import com.sts.dto.exam.ExamsBySpecificationReq;
import com.sts.dto.exam.GetExamsBySpecificationRes;
import com.sts.dto.exam.GetExamsBySubjectCodeRes;
import com.sts.dto.exam.GetExternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetInternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetSupplyExamsBySubjectCodeRes;
import com.sts.entity.Exam;
import com.sts.entity.SemesterSubject;
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
import com.sts.service.impl.validators.ObjectValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.ExamService;
import com.sts.specification.ExamSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;
	private final SemesterRepository semesterRepository;
	private final ValidatorRuleStatus validatorRuleService;
	private final ApplicationContext applicationContext;
	private final ExamRepository examRepository;
	private final StudentExamRepository studentExamRepository;
	private final SemesterSubjectRepository semesterSubjectRepository;
	private final StudentSubjectRepository studentSubjectRepository;

	public ExamServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper,
			FacultyRepository facultyRepository, DepartmentRepository departmentRepository,
			SemesterRepository semesterRepository, ValidatorRuleStatus validatorRuleService,
			ApplicationContext applicationContext, ExamRepository examRepository,
			StudentExamRepository studentExamRepository, SemesterSubjectRepository semesterSubjectRepository,
			StudentSubjectRepository studentSubjectRepository) {
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

	@Override
	public List<Exam> GetExamsBySpecification(ExamsBySpecificationReq req) {
		log.info("Fetching exams based on specification: {}", req);

		ObjectValidator.isObjectEmpty(req);

		List<Exam> exams = examRepository.findAll(ExamSpecification.getExamSpecification(req));

		if (exams.isEmpty()) {
			log.info("No Exams found for the given filter criteria.");
			throw new ResourceNotFoundException(
					ErrorMessageEnum.EXAMS_NOT_FOUND_FOR_SPECIFICATION.getMessage());
		}
		log.info("Successfully fetched {} students.", exams.size());
		return exams;
	}

//	    List<GetExamsBySpecificationRes> responses = exams.stream()
//	            .map(this::mapToGetExamBySpecificationRes)
//	            .collect(Collectors.toList());
	@Override
	public GetExamsBySpecificationRes mapExamToGetExamBySpecificationRes(Exam exam) {
		GetExamsBySpecificationRes response = modelMapper.map(exam, GetExamsBySpecificationRes.class);

		if (exam.getSemester() != null) {
			response.setSemesterCode(exam.getSemester().getSemesterCode());
		}
		if (exam.getSemesterSubject() != null) {
			response.setSubjectCode(exam.getSemesterSubject().getSubjectCode());
		}

		return response;
	}

	@Override
	public AddExamResponse createExam(AddExamRequest req) {

		// Exam exam = examRepository.getByExamCode(req.getExamCode()).orElseThrow(() ->
		// new
		// ResourceNotFoundException(ErrorMessageEnum.EXAM_NOT_FOUND.getMessage(req.getExamCode())));

		SemesterSubject semesterSubject = semesterSubjectRepository.findById(req.getSubjectCode())
				.orElseThrow(() -> new ResourceNotFoundException(
						ErrorMessageEnum.SEMESTER_SUBJECT_NOT_FOUND.getMessage(req.getSubjectCode())));
		Exam newExam = modelMapper.map(req, Exam.class);
		newExam.setSemesterSubject(semesterSubject);
		newExam.setSemester(semesterSubject.getSemester());

		Exam savedExam = examRepository.save(newExam);
		log.info("Saved Exam data: {}", newExam);

		AddExamResponse addExamResponse = modelMapper.map(savedExam, AddExamResponse.class);
		return addExamResponse;
	}

	@Override
	public String bulkCreateExams(List<AddExamRequest> requests) {
		if (requests == null || requests.isEmpty()) {
			throw new IllegalArgumentException("Exam request list is empty");
		}

		List<Exam> examsToSave = new ArrayList<>();

		for (AddExamRequest req : requests) {
			SemesterSubject semesterSubject = semesterSubjectRepository.findById(req.getSubjectCode())
					.orElseThrow(() -> new ResourceNotFoundException(
							ErrorMessageEnum.SEMESTER_SUBJECT_NOT_FOUND.getMessage(req.getSubjectCode())));
			if (examRepository.existsByExamCode(req.getExamCode())) {
				throw new DuplicateResourceException(
						ErrorMessageEnum.DUPLICATE_EXAM_CODE.getMessage(req.getExamCode()));
			}
			Exam exam = modelMapper.map(req, Exam.class);
			exam.setSemesterSubject(semesterSubject);
			exam.setSemester(semesterSubject.getSemester());

			examsToSave.add(exam);
		}

		List<Exam> savedExams = examRepository.saveAll(examsToSave);
		log.info("Saved {} exams.", savedExams.size());

		return "Saved exams count" + savedExams.size();
	}

	// @Override
	// public AddStudentsToSemRes addStudentsToExam(AddStudentsToSemReq req) {
	// log.info("Starting to add students to semester: {}", req);
	//
	// Semester semester = semesterRepository.findById(req.getSemesterCode())
	// .orElseThrow(() -> {
	// log.error("Semester not found with code: {}", req.getSemesterCode());
	// return new ResourceNotFoundException("Semester not found: " +
	// req.getSemesterCode());
	// });
	//
	// for (String studentId : req.getStudentIds()) {
	// Student student = studentRepository.findById(studentId)
	// .orElseThrow(() -> {
	// log.error("Student not found with ID: {}", studentId);
	// return new ResourceNotFoundException("Student not found: " + studentId);
	// });
	//
	// boolean alreadyExists = semester.getSemesterStudents().stream()
	// .anyMatch(ss -> ss.getStudent().getStudentId().equals(studentId));
	//
	// if (!alreadyExists) {
	// SemesterStudent semesterStudent = new SemesterStudent();
	// semesterStudent.setSemester(semester);
	// semesterStudent.setStudent(student);
	// semester.getSemesterStudents().add(semesterStudent);
	// log.debug("Mapped student {} to semester {}", studentId,
	// semester.getSemesterCode());
	// } else {
	// log.warn("Student {} already mapped to semester {}, skipping", studentId,
	// semester.getSemesterCode());
	// }
	// }
	//
	// Semester savedSemester = semesterRepository.save(semester);
	// log.info("Successfully added students to semester: {}",
	// savedSemester.getSemesterCode());
	//
	// AddStudentsToSemRes res = new AddStudentsToSemRes();
	// res.setSemesterCode(savedSemester.getSemesterCode());
	//
	// List<String> addedStudentIds = savedSemester.getSemesterStudents().stream()
	// .map(ss -> ss.getStudent().getStudentId())
	// .collect(Collectors.toList());
	//
	// res.setStudentIds(addedStudentIds);
	// log.info("Response prepared with {} student IDs.", addedStudentIds.size());
	//
	// return res;
	// }

	@Override
	public List<GetExamsBySubjectCodeRes> getExamsBySubjectCode(String subjectCode) {

//		List<Exam> exams = examRepository.findAllBySemesterSubject_SubjectCode(subjectCode);
		ExamsBySpecificationReq spec = new ExamsBySpecificationReq();
		spec.setSubjectCode(subjectCode);
		List<Exam> exams = GetExamsBySpecification(spec);
		exams.forEach(exam -> {
			log.info("Exam code: {}", exam.getExamCode());
		});

		List<GetExamsBySubjectCodeRes> subjectExams = exams.stream().map(exam -> {
			GetExamsBySubjectCodeRes subjectExam = new GetExamsBySubjectCodeRes();
			subjectExam.setExamCode(exam.getExamCode());
			subjectExam.setExamName(exam.getExamName());
			subjectExam.setExamSubType(exam.getExamSubType());
			subjectExam.setExamType(exam.getExamType());
			return subjectExam;
		}).collect(Collectors.toList());

		return subjectExams;
	}

	// Returns a list of Internal Exams
	@Override
	public List<GetInternalExamsBySubjectCodeRes> getInternalExamsBySubjectCode(String subjectCode) {

		ExamsBySpecificationReq spec = new ExamsBySpecificationReq();
		spec.setSubjectCode(subjectCode);
		spec.setExamType("internal");
		List<Exam> exams = GetExamsBySpecification(spec);

		exams.forEach(exam -> {
			log.info("Exam code: {}", exam.getExamCode());
		});

		List<GetInternalExamsBySubjectCodeRes> subjectExams = exams.stream().map(exam -> {
			GetInternalExamsBySubjectCodeRes subjectExam = new GetInternalExamsBySubjectCodeRes();
			subjectExam.setExamCode(exam.getExamCode());
			subjectExam.setExamName(exam.getExamName());
			subjectExam.setExamSubType(exam.getExamSubType());
			subjectExam.setExamType(exam.getExamType());
			return subjectExam;
		}).collect(Collectors.toList());

		return subjectExams;
	}

	@Override
	public List<GetExternalExamsBySubjectCodeRes> getExternalExamsBySubjectCode(String subjectCode) {

		ExamsBySpecificationReq spec = new ExamsBySpecificationReq();
		spec.setSubjectCode(subjectCode);
		spec.setExamType("external");
		spec.setExamSubType("regular");
		List<Exam> exams = GetExamsBySpecification(spec);

		exams.forEach(exam -> {
			log.info("Exam code: {}", exam.getExamCode());
		});

		List<GetExternalExamsBySubjectCodeRes> subjectExams = exams.stream().map(exam -> {
			GetExternalExamsBySubjectCodeRes subjectExam = new GetExternalExamsBySubjectCodeRes();
			subjectExam.setExamCode(exam.getExamCode());
			subjectExam.setExamName(exam.getExamName());
			subjectExam.setExamSubType(exam.getExamSubType());
			subjectExam.setExamType(exam.getExamType());
			return subjectExam;
		}).collect(Collectors.toList());

		return subjectExams;
	}

	@Override
	public List<GetSupplyExamsBySubjectCodeRes> getSupplyExamsBySubjectCode(String subjectCode, boolean onlyLatest) {
		log.info("Fetching supply exams for subjectCode: {}, onlyLatest: {}", subjectCode, onlyLatest);

		ExamsBySpecificationReq spec = new ExamsBySpecificationReq();
		spec.setSubjectCode(subjectCode);
		spec.setExamType("external");
		spec.setExamSubType("supply");

		log.info("Calling GetExamsBySpecification with spec: {}", spec);
		List<Exam> exams = GetExamsBySpecification(spec);

		if (exams == null || exams.isEmpty()) {
			log.info("No exams found for subjectCode: {}", subjectCode);
			return new ArrayList<>();
		}

		if (onlyLatest) {
			log.info("Filtering to get only the latest exam by date.");
			Optional<Exam> latestExam = exams.stream().filter(exam -> exam.getExamDate() != null)
					.max(Comparator.comparing(Exam::getExamDate));

			if (latestExam.isPresent()) {
				Exam exam = latestExam.get();
				log.info("Latest exam found: {} - {}", exam.getExamCode(), exam.getExamDate());
				GetSupplyExamsBySubjectCodeRes subjectExam = new GetSupplyExamsBySubjectCodeRes();
				subjectExam.setExamCode(exam.getExamCode());
				subjectExam.setExamName(exam.getExamName());
				subjectExam.setExamSubType(exam.getExamSubType());
				subjectExam.setExamType(exam.getExamType());
				subjectExam.setPassMarks(exam.getPassMarks());
				return List.of(subjectExam);
			} else {
				log.info("No valid exams with non-null examDate found.");
				return new ArrayList<>();
			}
		}

		log.info("Returning all supply exams for subjectCode: {}", subjectCode);
		return exams.stream().map(exam -> {
			log.debug("Mapping exam: {}", exam.getExamCode());
			GetSupplyExamsBySubjectCodeRes subjectExam = new GetSupplyExamsBySubjectCodeRes();
			subjectExam.setExamCode(exam.getExamCode());
			subjectExam.setExamName(exam.getExamName());
			subjectExam.setExamSubType(exam.getExamSubType());
			subjectExam.setExamType(exam.getExamType());
			subjectExam.setPassMarks(exam.getPassMarks());		
			return subjectExam;
		}).collect(Collectors.toList());
	}

	@Override
	// Overloaded method (defaults to false) which returns all supply exams for
	// given exam
	public List<GetSupplyExamsBySubjectCodeRes> getSupplyExamsBySubjectCode(String subjectCode) {
		boolean getOnlyLatestSupplyExam = true; // TODO: replace it with database fetching to change it dynamically.
		return getSupplyExamsBySubjectCode(subjectCode, getOnlyLatestSupplyExam);
	}

	@Override
	public List<GetExamsBySubjectCodeRes> getExamsBySubjectCodeAndExamTypeAndExamSubType(String subjectCode,
			String examType, String examSubType) {

		ExamsBySpecificationReq spec = new ExamsBySpecificationReq();
		spec.setSubjectCode(subjectCode);
		spec.setExamType(examType);
		spec.setExamSubType(examSubType);

		List<Exam> exams = GetExamsBySpecification(spec);

		exams.forEach(exam -> {
			log.info("Exam code {} | Exam Subtype {}", exam.getExamCode(), examSubType);
		});

		List<GetExamsBySubjectCodeRes> subjectExams = exams.stream().map(exam -> {
			GetExamsBySubjectCodeRes subjectExam = new GetExamsBySubjectCodeRes();
			subjectExam.setExamCode(exam.getExamCode());
			subjectExam.setExamName(exam.getExamName());
			subjectExam.setExamSubType(exam.getExamSubType());
			subjectExam.setExamType(exam.getExamType());
			subjectExam.setPassMarks(exam.getPassMarks());
			return subjectExam;
		}).collect(Collectors.toList());

		return subjectExams;
	}

}
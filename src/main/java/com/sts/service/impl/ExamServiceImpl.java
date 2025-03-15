package com.sts.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.constants.SuccessMessageEnum;
import com.sts.constants.ValidatorRulesEnum;
import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;
import com.sts.entity.Exam;
import com.sts.exceptions.BadRequestException;
import com.sts.exceptions.DatabaseException;
import com.sts.exceptions.FilterCriteriaException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.ExamRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.SemesterRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.impl.validators.ExamRequestValidator;
import com.sts.service.impl.validators.ValidatorRuleStatus;
import com.sts.service.interfaces.ExamService;
import com.sts.specification.ExamSpecification;
import com.sts.validator.Validator;

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

    public ExamServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, SemesterRepository semesterRepository, ValidatorRuleStatus validatorRuleService, ApplicationContext applicationContext, ExamRepository examRepository) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.semesterRepository = semesterRepository;
        this.validatorRuleService = validatorRuleService;
        this.applicationContext = applicationContext;
        this.examRepository = examRepository;
    }

    @Override
    public List<ExamResponse> getExams(ExamRequest filterRequest) {
        log.info("Fetching exam records based on filter criteria: {}", filterRequest);
        if (isFilterEmpty(filterRequest)) {
        	throw new FilterCriteriaException();
        }

        try {
            List<Exam> examList = examRepository.findAll(ExamSpecification.getExamSpec(filterRequest));

            if (examList.isEmpty()) {
                log.warn("No exam records found for the given filter criteria.");
                return Collections.emptyList();
            }

            List<ExamResponse> responseList = examList.stream()
                    .map(exam -> {
                        ExamResponse response = modelMapper.map(exam, ExamResponse.class);
                        response.setStudentId(exam.getStudent().getStudentId());
                        response.setSemesterCode(exam.getSemester().getSemesterCode());
                        return response;
                    })
                    .collect(Collectors.toList());

            log.info("Successfully fetched {} exam records.", responseList.size());
            return responseList;
        } catch (Exception ex) {
            log.error("Error occurred while fetching exams: {}", ex.getMessage());
            throw new DatabaseException(ErrorMessageEnum.FAILED_TO_FETCH_EXAMS.getMessage());
        }
    }

    @Override
    public ExamResponse saveExam(ExamRequest examRequest) {
    	
    	if (isFilterEmpty(examRequest)) {
            throw new FilterCriteriaException();
        }
        log.info("Saving new exam: {}", examRequest);

        if (validatorRuleService.isRuleActive(ValidatorRulesEnum.EXAM_REQUEST_VALIDATOR.getRuleName())) {
            Validator<ExamRequest> validator = applicationContext.getBean(ExamRequestValidator.class);
            validator.validate(examRequest);
        }

        try {
            Exam newExam = modelMapper.map(examRequest, Exam.class);
            newExam.setSemester(semesterRepository.getSemesterBySemesterCode(examRequest.getSemesterCode()));
            newExam.setStudent(studentRepository.getStudentByStudentId(examRequest.getStudentId()));

            Exam savedExam = examRepository.save(newExam);
            log.info("Exam saved successfully with ID: {}", savedExam.getExamCode());

            ExamResponse examResponse = modelMapper.map(savedExam, ExamResponse.class);
            examResponse.setSemesterCode(savedExam.getSemester().getSemesterCode());
            examResponse.setStudentId(savedExam.getStudent().getStudentId());

            return examResponse;
        } catch (Exception ex) {
            log.error("Error occurred while saving exam: {}", ex.getMessage(), ex);
            throw new DatabaseException(ErrorMessageEnum.FAILED_TO_SAVE_EXAM.getMessage());
        }
    }

    @Override
    public String updateExam(ExamUpdateRequest examUpdateRequest) {
    	

        log.info("Updating exam for student ID: {}, subject: {} and exam {}", 
                 examUpdateRequest.getStudentId(), examUpdateRequest.getSubjectCode(), examUpdateRequest.getExamName());

        try {
            int updated = examRepository.updateExam(
                    examUpdateRequest.getStudentId(),
                    examUpdateRequest.getSubjectCode(),
                    examUpdateRequest.getExamName(),
                    examUpdateRequest.getMarksObtained()
            );

            if (updated > 0) {
                log.info("Successfully updated exam.");
                return SuccessMessageEnum.EXAM_UPDATED_SUCCESSFULLY.getMessage();
            } else {
                
                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_UPDATE_EXAM.getMessage());
            }
        } catch (DatabaseException ex) {
            throw ex; 
        } catch (Exception ex) {
            log.error("Error occurred while updating exam");
            throw ex;
        }
    }

    @Override
    public String saveMultipleExams(List<ExamRequest> examRequests) {
        log.info("Saving multiple exams, count: {}", examRequests.size());

        try {
            List<Exam> exams = examRequests.stream()
                    .map(req -> {
                        Exam exam = modelMapper.map(req, Exam.class);
                        exam.setStudent(studentRepository.getStudentByStudentId(req.getStudentId()));
                        exam.setSemester(semesterRepository.getSemesterBySemesterCode(req.getSemesterCode()));
                        return exam;
                    })
                    .collect(Collectors.toList());

            List<Exam> savedExams = examRepository.saveAll(exams);

            if (savedExams.size() != examRequests.size()) {
                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_SAVE_EXAM.getMessage());
            }

            return SuccessMessageEnum.EXAMS_SAVED_SUCCESSFULLY.getMessage();
        }catch (DatabaseException ex) {
            throw ex; 
            
        }catch (Exception ex) {
            log.error("Error occurred while saving multiple exams");
            throw ex;
        }
    }

    @Override
    public String updateMultipleExams(List<ExamUpdateRequest> examUpdateRequests) {
        log.info("Updating multiple exams, count: {}", examUpdateRequests.size());

        try {
            int totalUpdated = 0;
            for (ExamUpdateRequest request : examUpdateRequests) {
                int updated = examRepository.updateExam(
                        request.getStudentId(),
                        request.getSubjectCode(),
                        request.getExamName(),
                        request.getMarksObtained()
                );
                if (updated > 0) {
                    totalUpdated++;
                }
            }

            if (totalUpdated == examUpdateRequests.size()) {
                log.info("Successfully updated all {} exams.", examUpdateRequests.size());
                return SuccessMessageEnum.EXAMS_UPDATED_SUCCESSFULLY.getMessage();
            } else {
                log.warn("Failed to update {} exams.", examUpdateRequests.size() - totalUpdated);
                throw new DatabaseException(ErrorMessageEnum.FAILED_TO_UPDATE_EXAM.getMessage());
            }
        }catch (DatabaseException ex) {
            throw ex; 
        }
        catch (Exception ex) {
            log.error("Error occurred while updating multiple exams");
            throw ex;
        }
    }

    private boolean isFilterEmpty(ExamRequest filterRequest) {
        return (filterRequest.getExamCode() == null 
                && filterRequest.getExamType() == null 
                && filterRequest.getExamName() == null 
                && filterRequest.getExamDate() == null 
                && filterRequest.getSubjectCode() == null 
                && filterRequest.getMarksObtained() == null 
                && filterRequest.getSemesterCode() == null 
                && filterRequest.getStudentId() == null);
    }
    
    
    
}
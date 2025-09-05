package com.sts.service.impl.validators;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.dto.student.StudentCreateRequest;
import com.sts.exceptions.BadRequestException;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentRequestValidator implements Validator<StudentCreateRequest> {

    private final StudentRepository studentRepository;
    private final ApplicationContext applicationContext;
    private final ValidatorRuleStatus validatorRuleStatus;

    public StudentRequestValidator(StudentRepository studentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleStatus) {
        this.studentRepository = studentRepository;
        this.applicationContext = applicationContext;
        this.validatorRuleStatus = validatorRuleStatus;
    }

    @Override
    public boolean validateAndGetResult(StudentCreateRequest object) {
        return false;
    }

    @Override
    public void validate(StudentCreateRequest studentRequest) {
        log.info("Starting validation for StudentRequest with Student ID: {}", studentRequest.getStudentId());

        if (studentRequest.getStudentId() == null || studentRequest.getStudentId().isEmpty()) {
            log.error("Validation failed: Student ID is missing");
            throw new BadRequestException(ErrorMessageEnum.STUDENT_ID_REQUIRED.getMessage());
        }

        if (validatorRuleStatus.isRuleActive(ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getRuleName())) {
            log.info("Validation rule '{}' is active, checking for duplicate student ID.", 
                     ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getRuleName());

            Validator<String> validator = (Validator<String>) applicationContext.getBean(
                ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getValidatorClass()
            );

            validator.validate(studentRequest.getStudentId());
        }

        log.info("Validation successful for Student ID '{}' and Department ID '{}'", 
                 studentRequest.getStudentId(), studentRequest.getDepartmentId());
    }
}

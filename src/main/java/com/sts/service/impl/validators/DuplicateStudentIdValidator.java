package com.sts.service.impl.validators;

import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.exceptions.ConflictException;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DuplicateStudentIdValidator implements Validator<String> {
    
    private final StudentRepository studentRepository;

    public DuplicateStudentIdValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void validate(String studentId) {
        log.info("Validating duplicate Student ID: {}", studentId);
        
        if (studentRepository.existsByStudentId(studentId)) {
            log.error("Validation failed: Duplicate Student ID detected - {}", studentId);
            throw new ConflictException(String.format(ErrorMessageEnum.DUPLICATE_STUDENT_ID.getMessage(), studentId));
        }

        log.info("Validation successful: No duplicate found for Student ID: {}", studentId);
    }

    @Override
    public boolean validateAndGetResult(String studentId) {
        return false;
    }
}

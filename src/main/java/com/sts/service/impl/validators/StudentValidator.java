package com.sts.service.impl.validators;

import org.springframework.stereotype.Service;

import com.sts.constants.ErrorMessageEnum;
import com.sts.exceptions.ConflictException;
import com.sts.exceptions.ResourceNotFoundException;
import com.sts.repository.StudentRepository;

@Service
public class StudentValidator {

    
    private final StudentRepository studentRepository;

    public StudentValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void validateDuplicateStudentId(String studentId) {
        if (studentRepository.existsByStudentId(studentId)) {
            throw new ConflictException(ErrorMessageEnum.DUPLICATE_STUDENT_ID.getMessage());
        }
    }

    public void validateStudentExists(String studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException(ErrorMessageEnum.STUDENT_NOT_FOUND.getMessage());
        }
    }
}

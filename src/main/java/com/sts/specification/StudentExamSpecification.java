package com.sts.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.sts.dto.exam.AddExamRequest;
import com.sts.entity.Exam;
import com.sts.entity.StudentExam;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class StudentExamSpecification {
    
    public static Specification<StudentExam> getExamSpec(AddExamRequest filterRequest) {
        return (Root<StudentExam> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by examCode (if provided)
            if (filterRequest.getExamCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examCode"), filterRequest.getExamCode()));
            }

            // Filter by examType (if provided)
            if (filterRequest.getExamType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examType"), filterRequest.getExamType()));
            }

            // Filter by examName (if provided)
            if (filterRequest.getExamName() != null) {
                predicates.add(criteriaBuilder.like(root.get("examName"), "%" + filterRequest.getExamName() + "%"));
            }

            // Filter by examDate (if provided)
            if (filterRequest.getExamDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examDate"), filterRequest.getExamDate()));
            }

            // Filter by subjectCode (if provided)
            if (filterRequest.getSubjectCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("subjectCode"), filterRequest.getSubjectCode()));
            }

            
           
          
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


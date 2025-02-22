package com.sts.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.sts.dto.ExamRequest;
import com.sts.entity.Exam;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class ExamSpecification {
    
    public static Specification<Exam> getExamSpec(ExamRequest filterRequest) {
        return (Root<Exam> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by examCode (if provided)
            if (filterRequest.getExamCode() != null) {
                predicates.add(cb.equal(root.get("examCode"), filterRequest.getExamCode()));
            }

            // Filter by examType (if provided)
            if (filterRequest.getExamType() != null) {
                predicates.add(cb.equal(root.get("examType"), filterRequest.getExamType()));
            }

            // Filter by examName (if provided)
            if (filterRequest.getExamName() != null) {
                predicates.add(cb.like(root.get("examName"), "%" + filterRequest.getExamName() + "%"));
            }

            // Filter by examDate (if provided)
            if (filterRequest.getExamDate() != null) {
                predicates.add(cb.equal(root.get("examDate"), filterRequest.getExamDate()));
            }

            // Filter by subjectCode (if provided)
            if (filterRequest.getSubjectCode() != null) {
                predicates.add(cb.equal(root.get("subjectCode"), filterRequest.getSubjectCode()));
            }

            // Filter by marksObtained (if provided)
            if (filterRequest.getMarksObtained() != null) {
                predicates.add(cb.equal(root.get("marksObtained"), filterRequest.getMarksObtained()));
            }

            // Filter by semesterCode (if provided)
            if (filterRequest.getSemesterCode() != null) {
                predicates.add(cb.equal(root.get("semester").get("semesterCode"), filterRequest.getSemesterCode()));
            }

            // Filter by studentId (if provided)
            if (filterRequest.getStudentId() != null) {
                predicates.add(cb.equal(root.get("student").get("studentId"), filterRequest.getStudentId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


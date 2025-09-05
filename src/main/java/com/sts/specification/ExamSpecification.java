package com.sts.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.sts.dto.exam.ExamsBySpecificationReq;
import com.sts.entity.Exam;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class ExamSpecification {

    public static Specification<Exam> getExamSpecification(ExamsBySpecificationReq req) {
        return (Root<Exam> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exam Code
            if (req.getExamCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examCode"), req.getExamCode()));
            }

            // Exam Type
            if (req.getExamType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examType"), req.getExamType()));
            }

            // Exam Sub Type
            if (req.getExamSubType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examSubType"), req.getExamSubType()));
            }

            // Exam Name (partial match)
            if (req.getExamName() != null) {
                predicates.add(criteriaBuilder.like(root.get("examName"), "%" + req.getExamName() + "%"));
            }

            // Exam Date
            if (req.getExamDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("examDate"), req.getExamDate()));
            }

            // Date Range (optional)
//            if (req.getFromDate() != null && req.getToDate() != null) {
//                predicates.add(criteriaBuilder.between(root.get("examDate"), req.getFromDate(), req.getToDate()));
//            }

            // Subject Code (nested inside semesterSubject)
            if (req.getSubjectCode() != null) {
                Join<Object, Object> subjectJoin = root.join("semesterSubject");
                predicates.add(criteriaBuilder.equal(subjectJoin.get("subjectCode"), req.getSubjectCode()));
            }

            // Semester Code (nested inside semester)
            if (req.getSemesterCode() != null) {
                Join<Object, Object> semesterJoin = root.join("semester");
                predicates.add(criteriaBuilder.equal(semesterJoin.get("semesterCode"), req.getSemesterCode()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
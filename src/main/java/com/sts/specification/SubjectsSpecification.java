package com.sts.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sts.dto.subjects.SubjectGetRequest;
import com.sts.entity.Department;
import com.sts.entity.Subjects;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SubjectsSpecification {
    public static Specification<Subjects> getSubjectSpec(SubjectGetRequest subjectRequest) {
        return (Root<Subjects> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (subjectRequest.getSubjectId() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("subjectId"), subjectRequest.getSubjectId()));
            }

            if (subjectRequest.getSubjectTitle() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get("subjectTitle"), "%" + subjectRequest.getSubjectTitle() + "%"));
            }

            if (subjectRequest.getSubjectShortForm() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get("subjectShortForm"), "%" + subjectRequest.getSubjectShortForm() + "%"));
            }

            if (subjectRequest.getCredits() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("credits"), subjectRequest.getCredits()));
            }

            if (subjectRequest.getDepartmentId() != null) {
                Join<Subjects, Department> departmentJoin = root.join("department", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(departmentJoin.get("departmentId"), subjectRequest.getDepartmentId()));
            }

            return predicate;
        };
    }
}
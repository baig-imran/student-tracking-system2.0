package com.sts.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sts.dto.student.GetStudentReq;
import com.sts.entity.Department;
import com.sts.entity.Faculty;
import com.sts.entity.Student;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class StudentSpecification {

    public static Specification<Student> getStudentSpec(GetStudentReq studentRequest) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();  // Start with a true condition

            // Dynamically add conditions based on the provided fields
            if (studentRequest.getStudentId() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("studentId"), studentRequest.getStudentId()));
            }

            if (studentRequest.getStudentName() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.like(root.get("studentName"), "%" + studentRequest.getStudentName() + "%"));
            }

            if (studentRequest.getFatherName() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.like(root.get("fatherName"), "%" + studentRequest.getFatherName() + "%"));
            }

            if (studentRequest.getFatherMobileNumber() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("fatherMobileNumber"), studentRequest.getFatherMobileNumber()));
            }

            if (studentRequest.getStudentMobileNumber() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("studentMobileNumber"), studentRequest.getStudentMobileNumber()));
            }

            if (studentRequest.getDepartmentId() != null) {
                Join<Student, Department> departmentJoin = root.join("department", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(departmentJoin.get("departmentId"), studentRequest.getDepartmentId()));
            }

            if (studentRequest.getMentorId() != null) {
                Join<Student, Faculty> facultyJoin = root.join("faculty", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(facultyJoin.get("facultyId"), studentRequest.getMentorId()));
            }

            if (studentRequest.getIsGraduated() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("isGraduated"), studentRequest.getIsGraduated()));
            }

            if (studentRequest.getRegulation() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("regulation"), studentRequest.getRegulation()));
            }

            if (studentRequest.getBatch() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("batch"), studentRequest.getBatch()));
            }

            return predicate;
        };
    }
}


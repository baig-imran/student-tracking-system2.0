package com.sts.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sts.dto.attendance.AttendanceRequest;
import com.sts.entity.Attendance;

import jakarta.persistence.criteria.Predicate;

public class AttendanceSpecification {
    
    public static Specification<Attendance> getAttendanceSpec(AttendanceRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Add predicates based on non-null fields
            if (filter.getDepartmentId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("departmentId"), filter.getDepartmentId()));
            }
            if (filter.getSubjectCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("subjectCode"), filter.getSubjectCode()));
            }
            if (filter.getAttendanceDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("attendanceDate"), filter.getAttendanceDate()));
            }
            if (filter.getPeriod() != null) {
                predicates.add(criteriaBuilder.equal(root.get("period"), filter.getPeriod()));
            }
            if (filter.getIsPresent() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPresent"), filter.getIsPresent()));
            }
            if (filter.getStudentId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("student").get("studentId"), filter.getStudentId()));
            }
            if (filter.getSemesterCode() != null) {
                predicates.add(criteriaBuilder.equal(root.get("semester").get("semesterCode"), filter.getSemesterCode()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

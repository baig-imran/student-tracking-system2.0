package com.sts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sts.entity.Attendance;
import com.sts.entity.Student;
import com.sts.entity.StudentExam;
import com.sts.repository.AttendanceRepository;
import com.sts.repository.StudentExamRepository;
import com.sts.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentPerformanceServiceImpl {

    private final StudentRepository studentRepo;
    private final AttendanceRepository attendanceRepo;
    private final StudentExamRepository studentExamRepo;

    // 1. Attendance underperformers
    public List<Map<String, Object>> getAttendanceUnderperformersByMentorId(double threshold) {
        List<Student> students = studentRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

//        for (Student s : students) {
//            List<Attendance> attendanceList = attendanceRepo.findBytId(s.getStudentId());
//            if (attendanceList.isEmpty()) continue;
//
//            long total = attendanceList.size();
//            long present = attendanceList.stream().filter(Attendance::getIsPresent).count();
//            double percentage = (present * 100.0) / total;
//
//            if (percentage < threshold) {
//                Map<String, Object> record = new HashMap<>();
//                record.put("studentId", s.getStudentId());
//                record.put("studentName", s.getStudentName());
//                record.put("attendancePercentage", percentage);
//                result.add(record);
//            }
//        }
        return result;
    }

    // 2. Internal exam underperformers
    public List<Map<String, Object>> getInternalExamUnderperformersByMentorId() {
		return null;
//        List<StudentExam> exams = studentExamRepo.findByExamExamType("INTERNAL");
//        return exams.stream()
//                .filter(se -> !Boolean.TRUE.equals(se.getIsPresent()) ||
//                        se.getMarksObtained() < se.getExam().getPassMarks())
//                .map(se -> Map.of(
//                        "studentId", se.getStudent().getStudentId(),
//                        "studentName", se.getStudent().getStudentName(),
//                        "examName", se.getExam().getExamName(),
//                        "marksObtained", se.getMarksObtained(),
//                        "passMarks", se.getExam().getPassMarks()
//                ))
//                .toList();
    }

    // 3. External exam underperformers
    public List<Map<String, Object>> getExternalExamUnderperformersByMentorId() {
		return null;
//        List<StudentExam> exams = studentExamRepo.findByExamExamType("EXTERNAL");
//        return exams.stream()
//                .filter(se -> !Boolean.TRUE.equals(se.getIsPresent()) ||
//                        se.getMarksObtained() < se.getExam().getPassMarks())
//                .map(se -> Map.of(
//                        "studentId", se.getStudent().getStudentId(),
//                        "studentName", se.getStudent().getStudentName(),
//                        "examName", se.getExam().getExamName(),
//                        "marksObtained", se.getMarksObtained(),
//                        "passMarks", se.getExam().getPassMarks()
//                ))
//                .toList();
    }

    // 4. Supplies (backlogs)
    public List<Map<String, Object>> getSupplies() {
		return null;
//        List<StudentExam> allExams = studentExamRepo.findAll();
//
//        Map<Student, Long> supplyCounts = allExams.stream()
//                .filter(se -> !Boolean.TRUE.equals(se.getIsPresent()) ||
//                        se.getMarksObtained() < se.getExam().getPassMarks())
//                .collect(Collectors.groupingBy(StudentExam::getStudent, Collectors.counting()));
//
//        return supplyCounts.entrySet().stream()
//                .map(entry -> Map.of(
//                        "studentId", entry.getKey().getStudentId(),
//                        "studentName", entry.getKey().getStudentName(),
//                        "supplyCount", entry.getValue()
//                ))
//                .toList();
    }
}

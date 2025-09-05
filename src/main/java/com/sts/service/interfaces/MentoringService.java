package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.mentoring.GetMentoringStudentsByFacultyIdRes;

public interface MentoringService {
	public List<GetMentoringStudentsByFacultyIdRes> getMentoringStudentsByMentorId(String facultyId);

}

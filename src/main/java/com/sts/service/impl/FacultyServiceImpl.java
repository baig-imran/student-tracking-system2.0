package com.sts.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sts.entity.Faculty;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.FacultyRepository;
import com.sts.repository.StudentRepository;
import com.sts.service.interfaces.FacultyService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacultyServiceImpl implements FacultyService {
	
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;
	private final FacultyRepository facultyRepository;
	private final DepartmentRepository departmentRepository;

	public FacultyServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper, FacultyRepository facultyRepository, DepartmentRepository departmentRepository) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
		this.facultyRepository = facultyRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public Faculty getFacultyByFacultyId(String facultyId) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}

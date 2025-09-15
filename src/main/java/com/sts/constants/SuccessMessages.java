package com.sts.constants;


public enum SuccessMessages {

	// Generic
	CREATED_SUCCESSFULLY("Successfully created"),
	UPDATED_SUCCESSFULLY("Successfully updated"),
	DELETED_SUCCESSFULLY("Successfully deleted"),
	FETCHED_SUCCESSFULLY("Data fetched successfully"),

	// --- CRUD Operations ---
	CREATE_ENTITY("Request received to create %s with %s "),
	ENTITY_CREATED("Successfully created %s with ID %s "),

	CREATE_BULK_ENTITIES("Request received to create %s %s "), //Request received to create x departments
	BULK_ENTITIES_CREATED("Successfully created %s %s "),

	FETCH_ENTITY("Request received to fetch %s with ID %s "),
	FETCH_ENTITY_WITH_ID("Fetching %s with ID %s "),
	ENTITY_FETCHED_WITH_ID("Fetched %s with ID: %s "),
	ENTITY_FETCHED("Successfully fetched %s with ID %s "),

	FETCH_ALL_ENTITIES("Request received to fetch all %s "),
	ALL_ENTITIES_FETCHED("Successfully fetched all %s (%s records) "),

	UPDATE_ENTITY("Request received to update %s with %s"),
	ENTITY_UPDATED("Successfully updated %s with ID %s "),

	UPDATE_BULK_ENTITIES("Request received to update %s %s "),
	BULK_ENTITIES_UPDATED("Successfully updated %s %s "),

	DELETE_ENTITY("Request received to delete %s with ID %s "),
	ENTITY_DELETED_WITH_ID("Successfully deleted %s with ID %s "),

	DELETE_BULK_ENTITIES("Request received to delete bulk %s "),
	BULK_ENTITIES_DELETED("Successfully deleted %s %s "),
	
	ENTITIES_FETCHED_BY_SPECIFICATION("SERVICE: Successfully fetched %s %s "),


	// Attendance
	ATTENDANCE_CREATED("Attendance created successfully"),
	ATTENDANCE_UPDATED("Attendance updated successfully"),
	ATTENDANCE_DELETED("Attendance deleted successfully"),
	ATTENDANCE_FETCHED("Attendance fetched successfully"),
	ATTENDANCES_FETCHED(" %s attendance records fetched successfully"),
	ATTENDANCES_BULK_CREATED(" %s attendance records created successfully"),
	ATTENDANCES_BULK_UPDATED(" %s attendance records updated successfully"),

	// Student
	STUDENT_CREATED("Student '%s' created successfully"),
	STUDENT_UPDATED("Student '%s' updated successfully"),
	STUDENT_DELETED("Student '%s' deleted successfully"),
	STUDENT_FETCHED("Student '%s' fetched successfully"),
	STUDENTS_FETCHED(" %s Students fetched successfully"),
	STUDENT_REGISTERED("Student '%s' registered successfully"),
	STUDENTS_BULK_CREATED(" %s students created successfully"),
	STUDENTS_BULK_UPDATED(" %s students updated successfully"),
	STUDENTS_BULK_DELETED(" %s students deleted successfully"),
	SUPPLY_EXAM_DETAILS_FETCHED("Supply exam details fetched successfully for StudentId: %s "),

	// Department
	DEPARTMENT_CREATED("Department '%s' created successfully"),
	DEPARTMENT_UPDATED("Department '%s' updated successfully"),
	DEPARTMENT_DELETED("Department '%s' deleted successfully"),
	DEPARTMENT_FETCHED("Department '%s' fetched successfully"),
	DEPARTMENTS_BULK_CREATED(" %s Departments created successfully"),

	// Faculty
	FACULTY_CREATED("Faculty '%s' created successfully"),
	FACULTY_UPDATED("Faculty '%s' updated successfully"),
	FACULTY_DELETED("Faculty '%s' deleted successfully"),
	FACULTY_FETCHED("Faculty '%s' fetched successfully"),
	FACULTIES_FETCHED(" %s faculties fetched successfully"),
	FACULTIES_BULK_CREATED(" %s faculties created successfully"),
	FACULTIES_BULK_UPDATED(" %s faculties updated successfully"),
	FACULTY_ASSIGNED_TO_SEMESTER("Faculty assigned to semester '%s' successfully"),

	// Semester
	SEMESTER_UPDATED("Semester '%s' updated successfully"),
	SEMESTER_DELETED("Semester '%s' deleted successfully"),
	SEMESTER_CREATED("Semester '%s' created successfully"),
	SEMESTER_FETCHED("Semester '%s' fetched successfully"),
	SEMESTERS_FETCHED(" %s semesters fetched successfully"),
	FACULTY_ADDED_TO_SEMESTER("Faculties added to semester successfully"),
	STUDENTS_ADDED_TO_SEMESTER("Students added to semester successfully"),
	SUBJECTS_ADDED_TO_SEMESTER("Subjects added to semester successfully"),
	SEMESTER_DATES_FETCHED("Semester '%s' Start&End dates fetched successfully"),

	// Subject
	SUBJECT_CREATED("Subject created successfully"),
	SUBJECTS_CREATED("Subjects created successfully"),
	SUBJECT_FETCHED("Subject fetched successfully"),
	SUBJECTS_FETCHED("Subjects fetched successfully"),
	SUBJECT_UPDATED("Subject updated successfully"),
	SUBJECT_DELETED("Subject deleted successfully"),

	// Exam
	EXAM_CREATED("Exam '%s' created successfully"),
	EXAM_UPDATED("Exam '%s' updated successfully"),
	EXAM_DELETED("Exam '%s' deleted successfully"),
	EXAM_FETCHED("Exam '%s' fetched successfully"),
	EXAM_PUBLISHED("Exam '%s' published successfully"),
	EXAMS_FETCHED(" %s exams fetched successfully"),
	EXAMS_BULK_CREATED(" %s exams created successfully"),
	EXAMS_BULK_UPDATED(" %s exams updated successfully"),

	// Bulk Operations
	BULK_STUDENT_UPLOAD_SUCCESS("Bulk student upload successful"),
	BULK_EXAM_UPLOAD_SUCCESS("Bulk exam upload successful"),
	BULK_SUBJECT_UPLOAD_SUCCESS("Bulk subject upload successful"),

	// Auth/User
	LOGIN_SUCCESS("Login successful"),
	LOGOUT_SUCCESS("Logout successful"),
	USER_CREATED("User '%s' created successfully"),
	USER_UPDATED("User '%s' updated successfully"),
	USER_DELETED("User '%s' deleted successfully"),
	USER_FETCHED("User '%s' fetched successfully"),
	USER_REGISTERED("User registered successfully"),

	// Student-Semester Operations
	STUDENT_SUBJECTS_ENROLLED("Student '%s' enrolled in subjects successfully"),


	// Supply Registration
	SUPPLY_REGISTERED_STUDENTS_FETCHED("Fetched supply registered students successfully"),
	SUPPLY_STUDENTS_REGISTERED("Students registered for supply exam successfully"),
	SUPPLY_STUDENTS_BULK_REGISTERED("Bulk students registered for supply exam successfully"),


	// Student Exam
	STUDENTS_ADDED_TO_EXAM("Students added to exam successfully"),
	MULTIPLE_EXAM_DATA_UPLOADED("Multiple subject exam data uploaded successfully"),
	EXAM_STUDENTS_FETCHED("Students for exam '%s' fetched successfully"),
	STUDENT_ALL_SEMESTER_EXAMS_FETCHED("All semester exam details for student '%s' fetched successfully"),
	STUDENT_ALL_SEMESTER_INTERNAL_EXAMS_FETCHED("All semester internal exam details for student '%s' fetched successfully"),
	INTERNAL_EXAM_QUALIFIED_STUDENTS_FETCHED("Qualified students for internal exam of subject '%s' fetched successfully"),
	INTERNAL_DISQUALIFIED_STUDENTS_FETCHED("Disqualified students for internal exam of subject '%s' fetched successfully"),
	SEE_FAILED_STUDENTS_FETCHED("Failed students for exam '%s' fetched successfully"),
	STUDENT_OVERALL_MARKS_FETCHED("Overall marks for student '%s' fetched successfully"),
	LOW_INTERNAL_MARKS_STUDENTS_FETCHED("Low internal marks students fetched successfully for facultyId: %s"),
	INTERNAL_MARKS_FETCHED("Internal marks fetched successfully"),
	LOW_EXTERNAL_MARKS_FETCHED("External internal marks students fetched successfully for facultyId: %s"),
	SUPPLY_STUDENTS_FETCHED_BY_FACULTY("Students with supply subjects fetched successfullyfor facultyId: %s"),

	// SemesterSubject
	FACULTY_SEMESTER_SUBJECTS_FETCHED("Active semester subjects for faculty '%s' fetched successfully"),
	SEMESTER_SUBJECT_STUDENTS_FETCHED("Students for semester subject fetched successfully"),
	ACTIVE_SEMESTER_SUBJECTS_FETCHED("Active semester subjects fetched successfully"),

	// StudentSubject
	STUDENTS_ADDED_TO_SUBJECT("Students added to subject successfully"),
	SUBJECT_STUDENTS_FETCHED("Students for subject '%s' fetched successfully"),

	// General Success Messages
	OPERATION_SUCCESS("Operation completed successfully"),
	DATA_UPDATED("Data updated successfully"),
	REQUEST_PROCESSED("Request processed successfully");

	private final String message;

	SuccessMessages(String message) {
		this.message = message;
	}

	public String getMessage(Object... args) {
		if (message.contains(" %s ") && (args == null || args.length == 0)) {
			return message.replaceAll(" '%s' ", "");  // This logic removes '%s' from the message, because if no argument is passed, then '%s' will be displayed as it is. 
		}
		return (args != null && args.length > 0) ? String.format(message, args) : message;
	}
}

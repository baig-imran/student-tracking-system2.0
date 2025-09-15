package com.sts.constants;

/**
 * An enum to centralize and standardize entity names.
 * This ensures consistency in logging, error messages, and
 * other parts of the application where entity names are used.
 */
public enum EntityNames {

	DEPARTMENT("Department"),
	DEPARTMENTS("Departments"),

	STUDENT("Student"),
	STUDENTS("Students"),

	FACULTY("Faculty"),
	FACULTIES("Faculties"),

	SEMESTER("Semester"),
	SEMESTERS("Semesters"),

	SUBJECT("Subject"),
	SUBJECTS("Subjects"),

	EXAM("Exam"),
	EXAMS("Exams"),

	ATTENDANCE("Attendance"),
	ATTENDANCES("Attendances"),

	USER("User"),
	USERS("Users"),

	SUPPLY("Supply"),
	SUPPLIES("Supplies");


    private final String name;

    EntityNames(String name) {
        this.name = name;
    }

    /**
     * Returns the singular, capitalized name of the entity.
     *
     * @return The entity name as a String.
     */
    public String getName() {
        return name;
    }
}


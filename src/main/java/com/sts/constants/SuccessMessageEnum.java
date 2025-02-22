package com.sts.constants;


public enum SuccessMessageEnum {
    EXAM_SAVED_SUCCESSFULLY("Exam saved successfully."),
    EXAM_UPDATED_SUCCESSFULLY("Exam updated successfully."),
    EXAMS_SAVED_SUCCESSFULLY("Multiple exams saved successfully."),
    EXAMS_UPDATED_SUCCESSFULLY("Multiple exams updated successfully."),
    EXAM_DELETED_SUCCESSFULLY("Exam deleted successfully."),
    EXAMS_DELETED_SUCCESSFULLY("Multiple exams deleted successfully.");

    private final String message;

    SuccessMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

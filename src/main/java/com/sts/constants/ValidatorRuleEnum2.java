package com.sts.constants;

import com.sts.service.impl.validators.DuplicateStudentIdValidator;
import com.sts.service.impl.validators.StudentRequestValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ValidatorRuleEnum2 {

//	STUDENT_VALIDATOR("STUDENT_VALIDATOR", StudentValidator.class),
//	DEPARTMENT_VALIDATOR("DEPARTMENT_VALIDATOR", DepartmentValidator.class),
//	SUBJECT_VALIDATOR("SUBJECT_VALIDATOR", SubjectValidator.class),
	DUPLICATE_STUDENT_ID_VALIDATOR("DUPLICATE_STUDENT_ID_VALIDATOR", DuplicateStudentIdValidator.class),
	STUDENT_REQUEST_VALIDATOR("STUDENT_REQUEST_VALIDATOR", StudentRequestValidator.class);

  private final String ruleName;
  private final Class<?> validatorClass;

  ValidatorRuleEnum2(String ruleName, Class<?> validatorClass) {
      this.ruleName = ruleName;
      this.validatorClass = validatorClass;
  }

  public String getRuleName() {
      return ruleName;
  }

  public Class<?> getValidatorClass() {
      return validatorClass;
  }

  public static ValidatorRuleEnum2 getValidatorRuleByRuleName(String ruleName) {
      for (ValidatorRuleEnum2 value : values()) {
          if (value.ruleName.equalsIgnoreCase(ruleName)) {
              return value;
          }
      }
      log.error("No validator found for rule: {}", ruleName); // Log missing rule
      throw new IllegalArgumentException("No validator found for rule: " + ruleName);
  }

}

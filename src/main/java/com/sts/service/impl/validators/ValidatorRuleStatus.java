package com.sts.service.impl.validators;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidatorRuleStatus {

    //private final ValidatorRuleRepository validatorRuleRepository; // Used later when moving to DB
    private Map<String, Boolean> validatorCache = new HashMap<>();

    @PostConstruct
    public void loadValidatorRules() {
    	log.info("--validation rules activated--");
        // Step 1: Load dummy data (temporary solution)
        validatorCache.put("STUDENT_ID_VALIDATOR", true);
        validatorCache.put("STUDENT_REQUEST_VALIDATOR", true);
        validatorCache.put("EXAM_REQUEST_VALIDATOR", true);
        validatorCache.put("DUPLICATE_STUDENT_ID_VALIDATOR", true);
        validatorCache.put("DUPLICATE_DEPARTMENT_ID_VALIDATOR", true);
        validatorCache.put("DUPLICATE_FACULTY_ID_VALIDATOR", true);

        // Step 2 (Future): Replace this with database call
        // List<ValidatorRule> rules = validatorRuleRepository.findAll();
        // validatorCache = rules.stream().collect(Collectors.toMap(ValidatorRule::getRuleName, ValidatorRule::isActive));
    }

    public boolean isRuleActive(String ruleName) {
        return validatorCache.getOrDefault(ruleName, false);
    }

    public void updateRuleStatus(String ruleName, boolean status) {
        validatorCache.put(ruleName, status);
    }
}


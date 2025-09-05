package com.sts.dto.department;

import lombok.Data;

@Data
public class DepartmentUpdateRequest {
    private String departmentId;
    private String departmentName;
}

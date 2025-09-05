package com.sts.dto.exam.supplyregistration;

import java.util.List;

import lombok.Data;

@Data
public class RegisterSupplyStudentsReq {
	
	String examCode;
	List<String> studentIds;
}

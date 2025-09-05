package com.sts.dto.student;

import java.util.List;

import lombok.Data;

@Data
public class GetStudentsByIdsReq {
	  private List<String> studentIds;

}

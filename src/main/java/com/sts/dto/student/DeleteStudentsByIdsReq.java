package com.sts.dto.student;

import java.util.List;
import lombok.Data;

@Data
public class DeleteStudentsByIdsReq {
	  private List<String> studentIds;

}

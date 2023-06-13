package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocRequestModel {
	
	private Integer headId;
	
	private String status;
	
	private java.util.Date startDate;
	
	private java.util.Date endDate;
	
	private Integer page;
	
	private String filterType;

	private Integer userId;

	private String declineReason;
	
}

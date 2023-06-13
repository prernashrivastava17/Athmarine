package com.athmarine.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineerServiceReportResponseModel {
	
	private String requestId;
	
	private String vessel;
	
	private String category;
	
	private String country;
	
	private String port;
	
	private Date ETA;

	private Date ETD;
	
	private Date serviceRequestOn;
	
	private Integer serviceRequestId;
	
	private String serviceReport;
	
	private String issueType;
	
	private String status;

}

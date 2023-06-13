package com.athmarine.request;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

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
public class BidEngineerRelationModel {
	
	private Integer id;
	
	
	private Integer bidId;
	
	
	private Integer engineerId;

	
	private boolean status;
	
	
	private Date serviceOn;
	
	
	private Date serviceEndOn;
	
	
	private String engineerStatus;
	
	
	private String serviceReport;
	
	private Integer serviceRequestId;

}

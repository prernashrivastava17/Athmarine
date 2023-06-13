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
public class WorkingCustomerModel {
	
    private Integer id;
	
	private Integer workingRate;
	
	private Integer workingRateOT1;
	
	private Integer workingRateOT2;
	
	private Integer estimatedWorkingHours;
	
	private String holidays;
	
	private Integer totalSum;

}

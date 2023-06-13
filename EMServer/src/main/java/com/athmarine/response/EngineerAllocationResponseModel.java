package com.athmarine.response;

import java.util.List;

import com.athmarine.entity.Bids;
import com.athmarine.request.BidsModel;
import com.athmarine.request.EngineerResponseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class EngineerAllocationResponseModel {
	
	private BidsModel bidmodel;
	
	private ServiceRequestResponseModel serviceRequestResponseModel;
	
	private EngineerResponseModel engineerResponseModel;
	
	private String poNumber;

}

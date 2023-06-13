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
public class TravelCostCustomerModel {
	
	private Integer id;

	private Integer lumpSumCost;

	private Integer airFare;

	private Integer transportation;

	private Integer engineerRatePerHour;

	private Integer totalTravelTime;

	private Integer travelTimeCost;

	private Integer totalSum;

}

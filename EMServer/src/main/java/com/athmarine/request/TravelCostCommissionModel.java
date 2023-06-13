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
public class TravelCostCommissionModel {

	private Integer id;

	private Integer lumpSumCommission;

	private Integer airFareCommission;

	private Integer transportationCommission;

	private Integer travelTimeCostCommission;

	private Integer totalSum;

	private int status;

}

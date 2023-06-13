package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetTravelCostQuotationResponse {

	private Integer id;

	private Integer lumpSumCost;

	private Integer airFare;

	private Integer transportation;

	private Integer engineerRatePerHour;

	private Integer totalTravelTime;

	private Integer travelTimeCost;

	private Integer totalSum;
}

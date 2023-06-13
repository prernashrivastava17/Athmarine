package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetMiscellaneousQuotationResponse {

	private Integer id;

	private Integer portCharges;

	private Integer covidTestCharge;

	private Integer shipyardCharges;

	private Integer shipyardSurcharge;

	private Integer otherCharge;

	private Integer totalSum;
	
}

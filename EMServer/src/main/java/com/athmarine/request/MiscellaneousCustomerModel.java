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
public class MiscellaneousCustomerModel {
	
	private Integer id;

	private Integer portCharges;

	private Integer covidTestCharge;

	private Integer shipyardCharges;

	private Integer shipyardSurcharge;

	private Integer otherCharge;

	private Integer totalSum;

}

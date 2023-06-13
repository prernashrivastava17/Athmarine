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
public class MiscellaneousCommissionModel {

	private Integer id;

	private Integer portChargesCommission;

	private Integer covidTestChargeCommission;

	private Integer shipyardChargesCommission;

	private Integer shipyardSurchargeCommission;

	private Integer otherChargeCommission;

	private Integer totalSum;
	
	private int status;

}

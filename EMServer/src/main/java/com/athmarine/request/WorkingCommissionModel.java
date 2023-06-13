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
public class WorkingCommissionModel {

	private Integer id;

	private Integer workingRateTotalCommission;

	private Integer workingRateOT1TotalCommission;

	private Integer workingRateOT2TotalCommission;

	private Integer holidaysWorkingRateTotalCommission;

	private Integer holidaysWorkingRateOT1TotalCommission;

	private Integer holidaysWorkingRateOT2TotalCommission;

	private int status;
}

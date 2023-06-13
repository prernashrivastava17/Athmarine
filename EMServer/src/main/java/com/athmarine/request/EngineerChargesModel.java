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
public class EngineerChargesModel {

	private Integer id;

	private Integer basePrice;

	private Integer extraPrice;

	private Integer extraAbove;

	private Integer maxCharge;

	private Integer companyId;

	private Integer baseEngeerCount;

	private Integer extraEngeerCount;

	private Integer extraAboveEngeerCount;

	private Integer maxChargeEngeerCount;
}

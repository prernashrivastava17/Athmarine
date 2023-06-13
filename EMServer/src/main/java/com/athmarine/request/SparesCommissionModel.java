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
public class SparesCommissionModel {

	private Integer id;

	private Integer itemOneCommission;

	private Integer itemTwoCommission;

	private Integer itemThreeCommission;

	private Integer itemFourCommission;

	private Integer itemFiveCommission;

	private Integer totalSum;

	private int status;
}

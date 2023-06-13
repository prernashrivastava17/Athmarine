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
public class SparesVendorModel {

	private Integer id;

	private Integer itemOne;

	private Integer itemTwo;

	private Integer itemThree;

	private Integer itemFour;

	private Integer itemFive;

	private Integer totalSum;
	
	private Integer bidderId;

}

package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetSparesQuotationResponse {

	private Integer id;

	private Integer itemOne;

	private Integer itemTwo;

	private Integer itemThree;

	private Integer itemFour;

	private Integer itemFive;

	private Integer totalSum;
}

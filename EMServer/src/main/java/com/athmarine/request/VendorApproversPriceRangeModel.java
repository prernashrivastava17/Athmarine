package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorApproversPriceRangeModel {
	
	private Integer id;
	
	private float minValue;

	private float maxValue;
	
	private Integer approvedId;
	
	private int status;

}

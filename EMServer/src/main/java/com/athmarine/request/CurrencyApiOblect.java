package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyApiOblect {
	
	private Integer id;
	
	private String currency;
	
	private String currencySymbol;
	
	private Double rate;

}

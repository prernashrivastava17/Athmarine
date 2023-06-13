package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalAmount {
	
	private Integer totalEngineer;
	
	private Double totalAmount;
	
	private String invoiceNumber;
	
	private String name;
	
	private String address;

}

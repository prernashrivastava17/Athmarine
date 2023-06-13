package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MasterContinentModel {
	
	private Integer id;
	
	private String name;
	
	private String shortName;

	private int status;

}

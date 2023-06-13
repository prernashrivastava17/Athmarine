package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContinentModel {
	
	private Integer id;
	
	private String name;

	private int status;
	
	private List<CountryModel> masterCountry;

}

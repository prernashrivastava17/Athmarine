package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterCountryRequestModel {
	
    private Integer id;
	
	private String name;
	
	private String phoneCode;
	
	private String shortName;
	
	private String currency;
	
	private String flag;
	
	private int status;
	
	private List<MasterPortModel> portId;

}

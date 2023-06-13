package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentsModel {
	
    private Integer id;
	
	private String name;
	
	private String code;
	
	private String isVerified;
	
	private Integer status;

}

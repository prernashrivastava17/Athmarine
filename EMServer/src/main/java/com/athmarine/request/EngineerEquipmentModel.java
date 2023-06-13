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
public class EngineerEquipmentModel {
	
	private Integer id;
	
    private Integer status;
    
    private String certified;
	
	private String engineerId;
	
	private UserModel userModel;
	
	private String yesOrNo;

}

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
public class EquipmentCategoryRequestModel {
	
   private Integer id;
	
	private String name;
	
	private String shortName;

	private int status;

	private List<ServiceDetailsModel> serviceDetailsModels;

}

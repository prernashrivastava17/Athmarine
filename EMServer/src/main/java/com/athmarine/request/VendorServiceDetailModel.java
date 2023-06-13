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
public class VendorServiceDetailModel {
	
	private Integer id;

	private int status;
	
	private Integer companyId;
	
	private Boolean vendorServiceKey;
	
	private MasterCountryRequestModel country;
	
	private List<EquipmentCategoryModel> equipmentCategory;
	
}

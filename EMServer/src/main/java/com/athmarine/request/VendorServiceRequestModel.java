package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorServiceRequestModel {

	private Integer id;

	private int status;
	
	private Integer companyId;

	private List<VendorServiceDetailsModel> serviceDetails;

	private List<VendorServiceMasterPortsModel> portId;

	// private MasterCountryModel countryId;

}

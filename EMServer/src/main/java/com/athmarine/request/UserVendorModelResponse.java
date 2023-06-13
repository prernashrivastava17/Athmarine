package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVendorModelResponse {
	
    private UserModel userModel;
	
	//private VendorCompanyModel vendorCompanyModel;
	
	private VendorCompanysModel vendorCompanysModel;

}

package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorServiceResponseModel {

	private Integer id;

	private int status;

	private Integer companyId;

	private Boolean vendorServiceKey;

}

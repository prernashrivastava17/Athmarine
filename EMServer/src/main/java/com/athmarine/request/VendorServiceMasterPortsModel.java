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
public class VendorServiceMasterPortsModel {

	private Integer id;

	private String name;

	private String shortName;

	private int status;

	private boolean isVerified;

	private MasterStateModel mstateStateId;

	private CountryModel countryModel;

}

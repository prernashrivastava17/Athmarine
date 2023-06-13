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
public class VesselRequestModel {

	private Integer id;

	private String vesselName;

	private String vesselType;

	private Integer IMO;


	/*private String MMSI;

	private Integer shipId;

	private String callSign;

	private String typeName;

	private Integer DWT;

	private String flag;

	private String country;

	private String yearOfBuilt;

	private String MT_URL;

	private int status;*/
}

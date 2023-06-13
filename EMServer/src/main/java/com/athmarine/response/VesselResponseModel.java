package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VesselResponseModel {

	private Integer id;

	private String vesselName;

	private String vesselType;

	private Integer IMO;

}

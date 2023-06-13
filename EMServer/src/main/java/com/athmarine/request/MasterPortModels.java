package com.athmarine.request;

import java.util.List;

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
public class MasterPortModels {

	private Integer id;

	private String name;

	private String shortName;

	private Integer status;

	private List<MasterStatesModel> masterStateId;

	private CountryModel countryModel;
}

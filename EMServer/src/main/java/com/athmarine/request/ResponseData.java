package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ResponseData {

	Boolean valid;

	private String base;

	Long updated;

	@JsonProperty("rates")
	Rates rates;

}

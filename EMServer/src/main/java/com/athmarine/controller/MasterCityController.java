package com.athmarine.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MasterCityService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterCityInterfaceUri.MASTER_CITY_BASE_URI)
public class MasterCityController {

	@Autowired
	MasterCityService masterCityService;

	@GetMapping(RestMappingConstants.MasterCityInterfaceUri.MASTER_CITY_GET_URI + "/{id}")
	public ResponseEntity<?> getAllCityByStateId(@NotNull @PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterCityService.getAllCityByStateId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.MasterCityInterfaceUri.MASTER_CITY_GET_ALL_URI + "/{id}")
	public ResponseEntity<?> getAllCityByCountryId(@NotNull @PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterCityService.getAllCityByCountryId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

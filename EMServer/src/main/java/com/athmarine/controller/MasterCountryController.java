package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CountryDetailsService;
import com.athmarine.service.MasterCountryService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterCountryInterfaceUri.MASTER_COUNTRY_BASE_URI)
public class MasterCountryController {

	@Autowired
	MasterCountryService masterCountryService;

	@Autowired
	CountryDetailsService countryDetailsService;

	@GetMapping(RestMappingConstants.MasterCountryInterfaceUri.MASTER_COUNTRYS_GET_URI)
	public ResponseEntity<?> getAllCountry() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterCountryService.getAllCountry());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.MasterCountryInterfaceUri.GET_ALL_COUNTRY_DETAILS_URI)
	public ResponseEntity<?> getAllCountryDetails() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(countryDetailsService.getAllCountryDetails());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.MasterCountryInterfaceUri.MASTER_COUNTRY_CURRENCY_GET_URI)
	public ResponseEntity<?> getAllContryCurrencySymboll() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterCountryService.getAllContryCurrencySymbol());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

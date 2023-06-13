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
import com.athmarine.service.CurrencyApiService;

@RestController
@RequestMapping(path = RestMappingConstants.CurrencyApiInterfaceUri.CURRENCY_API_BASE_URI)
public class CurrencyApiController {

	@Autowired
	CurrencyApiService currencyApiService;

	@GetMapping(RestMappingConstants.CurrencyApiInterfaceUri.CURRENCY_API_GET_URI)
	public ResponseEntity<?> getAllCurrencyRate() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(currencyApiService.getAllCountryCurrency());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

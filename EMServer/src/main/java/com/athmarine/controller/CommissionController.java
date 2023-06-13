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
import com.athmarine.service.CommissionService;

@RestController
@RequestMapping(path = RestMappingConstants.CommissionInterfaceUri.COMMISSION_BASE_URI)
public class CommissionController {

	@Autowired
	CommissionService commissionService;

	@GetMapping(path = RestMappingConstants.CommissionInterfaceUri.COMMISSION_GET_ALL_URI)
	public ResponseEntity<BaseApiResponse> getAllCommission() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(commissionService.getAllCommission());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

package com.athmarine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.CustomerHeadModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerHeadService;

@RestController
@RequestMapping(path = RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_BASE_URI)
public class CustomerHeadController {

	@Autowired
	CustomerHeadService customerHeadService;

	@PostMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_HEAD_ADD_URI)
	public ResponseEntity<?> createCustomerHead(@RequestBody List<CustomerHeadModel> customerHeadModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerHeadService.addCustomerHead(customerHeadModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_HEAD_GET_ALL_APPROVER_URI + "/{id}")
	public ResponseEntity<?> getAllCustomerApprover(@PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerHeadService.getAllApproverByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_HEAD_GET_ALL_HEAD_URI + "/{id}")
	public ResponseEntity<?> getAllCustomerManagingHead(@PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerHeadService.getAllManagingHeadByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

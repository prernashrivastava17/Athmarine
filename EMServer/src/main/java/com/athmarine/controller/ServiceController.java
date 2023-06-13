package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.request.VendorServiceDetailModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorServicess;

@RestController
@RequestMapping(RestMappingConstants.ServiceInterfaceUri.SERVICE_BASE_URI)
public class ServiceController {

	@Autowired
	VendorServicess demoServices;

	// *** API To Create Service
	@PostMapping(RestMappingConstants.ServiceInterfaceUri.SERVICE_CREATE_URI)
	public ResponseEntity<?> createDemoService(@RequestBody VendorServiceDetailModel serviceModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				demoServices.createServices(serviceModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// GET API TO FETCH Get Service

	@GetMapping(RestMappingConstants.ServiceInterfaceUri.SERVICE_GET_URI + "/{id}")
	public ResponseEntity<?> getServiceByCompanyId(@PathVariable("id") int id) throws CustomerCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(demoServices.getVendorServiceByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

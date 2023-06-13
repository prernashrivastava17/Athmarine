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

import com.athmarine.request.WorkingVendorModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.WorkingService;

@RestController
@RequestMapping(path = RestMappingConstants.WorkingVendorInterfaceUri.WORKING_VENDOR_CREATE_URI)
public class WorkingController {

	@Autowired
	WorkingService workingService;

	@PostMapping(path = RestMappingConstants.WorkingVendorInterfaceUri.WORKING_VENDOR_CREATE_URI)
	public ResponseEntity<BaseApiResponse> createWorkingVendor(@RequestBody WorkingVendorModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(workingService.createWorkingVendor(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
//	@GetMapping(RestMappingConstants.WorkingVendorInterfaceUri.GET_WORKING_VENDOR_URI + "/{bidderId}")
//	public ResponseEntity<?> getWorkingVendorById(@PathVariable("bidderId") int bidderId) {
//
//		BaseApiResponse baseApiResponse = ResponseBuilder
//				.getSuccessResponse(workingService.getWorkingVendorById(bidderId));
//
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//	}

}

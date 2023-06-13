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

import com.athmarine.request.SparesVendorModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.SparesVendorService;

@RestController
@RequestMapping(path = RestMappingConstants.SparesVendorInterfacUri.SPARES_VENDOR_BASE_URI)
public class SparesVendorController {

	@Autowired
	SparesVendorService sparesVendorService;

	@PostMapping(path = RestMappingConstants.SparesVendorInterfacUri.SPARES_VENDOR_ADD_URI)
	public ResponseEntity<BaseApiResponse> createSparesVendor(@RequestBody SparesVendorModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(sparesVendorService.createSparesVendor(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.SparesVendorInterfacUri.GET_SPARES_VENDOR_URI + "/{bidderId}")
	public ResponseEntity<?> getVendorSparesById(@PathVariable("bidderId") int bidderId) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(sparesVendorService.getVendorSparesById(bidderId));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

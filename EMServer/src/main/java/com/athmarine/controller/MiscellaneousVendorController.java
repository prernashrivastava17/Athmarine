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

import com.athmarine.request.MiscellaneousVendorModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MiscellaneousVendorService;

@RestController
@RequestMapping(path = RestMappingConstants.MiscellaneousVendorInterfacUri.MISCELLANEOUS_VENDOR_BASE_URI)
public class MiscellaneousVendorController {

	@Autowired
	MiscellaneousVendorService miscellaneousVendorService;

//	@PostMapping(path = RestMappingConstants.MiscellaneousVendorInterfacUri.MISCELLANEOUS_VENDOR_ADD_URI)
//	public ResponseEntity<BaseApiResponse> createDepartmentName(@RequestBody MiscellaneousVendorModel model) {
//
//		BaseApiResponse baseApiResponse = ResponseBuilder
//				.getSuccessResponse(miscellaneousVendorService.createMiscellaneousVendor(model));
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//
//	}
//
//	@GetMapping(RestMappingConstants.MiscellaneousVendorInterfacUri.GET_MISCELLANEOUS_VENDOR_URI + "/{bidderId}")
//	public ResponseEntity<?> getMiscellaneousVendorById(@PathVariable("bidderId") int bidderId) {
//
//		BaseApiResponse baseApiResponse = ResponseBuilder
//				.getSuccessResponse(miscellaneousVendorService.getMiscellaneousVendorById(bidderId));
//
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//	}

}

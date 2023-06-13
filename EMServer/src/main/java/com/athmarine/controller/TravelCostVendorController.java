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

import com.athmarine.request.TravelCostVendorModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.TravelCostVendorService;

@RestController
@RequestMapping(path = RestMappingConstants.TravelCostVendorInterfacUri.TRAVEL_COST_VENDOR_BASE_URI)
public class TravelCostVendorController {

	@Autowired
	TravelCostVendorService travelCostVendorService;

//	@PostMapping(path = RestMappingConstants.TravelCostVendorInterfacUri.TRAVEL_COST_VENDOR_VENDOR_ADD_URI)
//	public ResponseEntity<BaseApiResponse> createTravelCostVendor(@RequestBody TravelCostVendorModel model) {
//
//		BaseApiResponse baseApiResponse = ResponseBuilder
//				.getSuccessResponse(travelCostVendorService.createTravelCostVendor(model));
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//
//	}
//
//	@GetMapping(RestMappingConstants.TravelCostVendorInterfacUri.GET_TRAVEL_COST_VENDOR_URI + "/{bidderId}")
//	public ResponseEntity<?> getVendorTravelCostById(@PathVariable("bidderId") int bidderId) {
//
//		BaseApiResponse baseApiResponse = ResponseBuilder
//				.getSuccessResponse(travelCostVendorService.getVendorTravelCostById(bidderId));
//
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//	}

}

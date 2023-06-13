package com.athmarine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.VesselRequestModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VesselService;

@RestController
@RequestMapping(path = RestMappingConstants.VesselInterfacUri.VESSEL_BASE_URI)
public class VesselController {

	@Autowired
	VesselService vesselService;

	@PostMapping(path = RestMappingConstants.VesselInterfacUri.VESSEL_ADD_URI)
	public ResponseEntity<BaseApiResponse> createAvailableOn(@RequestBody List<VesselRequestModel> modelList) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vesselService.createVessel(modelList));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(path = RestMappingConstants.VesselInterfacUri.VESSEL_GET_ALL_SHIP_NAME_AND_IMO_URI)
	public ResponseEntity<BaseApiResponse> getAllVesselIMOAndShipName(@RequestParam(defaultValue = "empty") Integer IMO, @RequestParam(defaultValue = "empty") String shipName) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vesselService.getAllVessel(IMO,shipName));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.VesselInterfacUri.VESSEL_GET_URI)
	public ResponseEntity<?> getAllVessel() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vesselService.getAllVessel());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.EngineerChargesModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EngineerChargesService;

@RestController
@RequestMapping(path = RestMappingConstants.EngineerChargesInterfaceUri.ENGINEER_CHARGES_BASE_URI)
public class EngineerChargesController {

	@Autowired
	EngineerChargesService engineerChargesService;

	@PostMapping(RestMappingConstants.EngineerChargesInterfaceUri.ENGINEER_CHARGES_CREATE_URI)
	public ResponseEntity<?> createEngineerEquipments(@RequestBody EngineerChargesModel engineerChargesModel) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerChargesService.createEngineerCharges(engineerChargesModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EngineerChargesInterfaceUri.ENGINEER_CHARGES_GET_URI)
	public ResponseEntity<?> getEngineerCharges() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerChargesService.findEngineerChargesById());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.EngineerChargesInterfaceUri.ENGINEER_CHARGES_UPDATE_URI)
	public ResponseEntity<?> updateEngineerCharges(@RequestBody EngineerChargesModel engineerChargesModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerChargesService.updateEngineerCharges(engineerChargesModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

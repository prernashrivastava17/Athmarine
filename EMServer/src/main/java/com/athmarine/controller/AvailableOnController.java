package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.AvailableOnModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.AvailableOnService;

@RestController
@RequestMapping(path = RestMappingConstants.AvailableOnInterfacUri.AVAILABLE_ON_BASE_URI)
public class AvailableOnController {

	@Autowired
	AvailableOnService availableOnService;

	@PostMapping(path = RestMappingConstants.AvailableOnInterfacUri.AVAILABLE_ON_ADD_URI)
	public ResponseEntity<BaseApiResponse> createAvailableOn(@RequestBody AvailableOnModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(availableOnService.createAvailableOn(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(path = RestMappingConstants.AvailableOnInterfacUri.AVAILABLE_ON_GET_URI)
	public ResponseEntity<BaseApiResponse> getAllAvailableOn() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(availableOnService.getAllAvailableOn());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

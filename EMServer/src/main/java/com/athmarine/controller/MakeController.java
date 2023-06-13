package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.BidsNotFoundException;
import com.athmarine.request.MakeModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MakeService;

@RestController
@RequestMapping(path = RestMappingConstants.MakeInterfaceUri.MAKE_BASE_URI)
public class MakeController {

	@Autowired
	public MakeService makeService;

	@PostMapping(RestMappingConstants.MakeInterfaceUri.MAKE_CREATE_URI)
	public ResponseEntity<?> createMake(@RequestBody MakeModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(makeService.createMake(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.MakeInterfaceUri.MAKE_GET_ALL_URI)
	public ResponseEntity<?> getAllMake() throws BidsNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(makeService.getAllMake());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

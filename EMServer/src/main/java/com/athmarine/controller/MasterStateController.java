package com.athmarine.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MasterStateService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterStateInterfaceUri.MASTER_STATE_BASE_URI)
public class MasterStateController {

	@Autowired
	MasterStateService masterStateService;

	@GetMapping(RestMappingConstants.MasterStateInterfaceUri.MASTER_STATE_GET_URI + "/{id}")
	public ResponseEntity<?> getMasterCountryById(@NotNull @PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterStateService.getAllStatesByCountryId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

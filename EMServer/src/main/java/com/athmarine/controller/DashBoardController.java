package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.DashBoardService;

@RestController
@RequestMapping(path = RestMappingConstants.DashBoardInterfacUri.DASHBOARD_BASE_URI)
public class DashBoardController {

	@Autowired
	public DashBoardService dashBoardService;

	@GetMapping(RestMappingConstants.DashBoardInterfacUri.REQUESTER_DASHBOARD_URI + "/{id}")
	public ResponseEntity<?> getRequesterDashboard(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(dashBoardService.getRequesterDashboard(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

package com.athmarine.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.MasterPortsModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MasterPortsService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_BASE_URI)
public class MasterPortsController {

	@Autowired
	MasterPortsService masterPortsService;

	@GetMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_GETS_URI + "/{id}")
	public ResponseEntity<?> getAllPortsByCountryId(@NotNull @PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPortsService.getAllPortsByCountryId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_GETS_All_URI)
	public ResponseEntity<?> getAllPorts() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPortsService.getAllPortsNonVerified());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PostMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_CREATE_URI)
	public ResponseEntity<BaseApiResponse> createMasterPort(@RequestBody List<MasterPortsModel> masterPortsModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPortsService.createMasterPort(masterPortsModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_VERIFIED_URI)
	public ResponseEntity<BaseApiResponse> verifiedMasterPort() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterPortsService.verifiedMasterPort());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_UPDATE_URI)
	public ResponseEntity<?> updateMasterPort(@RequestBody MasterPortsModel masterPortsModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPortsService.updateMasterPort(masterPortsModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.MasterPortsInterfaceUri.MASTER_PORTS_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteMasterPort(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterPortsService.deleteMasterPort(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

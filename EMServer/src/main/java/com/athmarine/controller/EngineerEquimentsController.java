package com.athmarine.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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

import com.athmarine.exception.EquipmentNotFoundException;
import com.athmarine.request.EngineerEquimentsModel;
import com.athmarine.request.EngineerEquipmentModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EngineerEquimentService;

@RestController
@RequestMapping(path = RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_BASE_URI)
public class EngineerEquimentsController {

	@Autowired
	EngineerEquimentService engineerEquimentService;

	@PostMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_CREATE_URI)
	public ResponseEntity<?> createEngineerEquipments(
			@RequestBody List<EngineerEquimentsModel> engineerEquimentsModel) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerEquimentService.createEngineerEquipments(engineerEquimentsModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_GET_URI + "/{id}")
	public ResponseEntity<?> getAllEngineerEquipmentsByCompanyId(@PathVariable("id") int id)
			throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerEquimentService.getEngineerEquipmentByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_GETS_URI + "/{id}")
	public ResponseEntity<?> getEngineerEquipmentsByUser(@PathVariable("id") int id) throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(engineerEquimentService.getEngineerId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_UPDATE_URI)
	public ResponseEntity<?> updateEngineerEquipment(
			@Valid @RequestBody EngineerEquipmentModel engineerEquipmentModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerEquimentService.updateEngineerEquipment(engineerEquipmentModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_GET_URI + "/{id}")
	public ResponseEntity<?> deleteEngineerEquipments(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerEquimentService.deleteEngineerEquipments(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.EngineerEquimentsInterfaceUri.ENGINEER_EQUIMENTS_BY_UID_GET_URI + "/{id}")
	public ResponseEntity<?> getEngineerEquipmentsByUid(@PathVariable("id") String uid) throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(engineerEquimentService.getEngineerByUid(uid));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}

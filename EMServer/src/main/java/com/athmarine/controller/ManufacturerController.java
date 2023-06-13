package com.athmarine.controller;

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

import com.athmarine.exception.ManufacturerNotFoundException;
import com.athmarine.request.ManufacturerModule;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.ManufacturerService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_BASE_URI)
public class ManufacturerController {

	@Autowired
	ManufacturerService manufacturerService;

	@GetMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_GET_URI + "/{id}")
	public ResponseEntity<?> getEquipmentManufacturer(@PathVariable("id") int id) throws ManufacturerNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.findManufacturerById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_GET_ALL_URI)
	public ResponseEntity<?> getAllEquipmentManufacturer() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.getAllEquipmentManufacturer());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_GET_ALL_BY_EQUIPMENT_ID_URI
			+ "/{id}")
	public ResponseEntity<?> getEquipmentManufacturerByEquipmentId(@PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.getAllManufacturerByEquipmentId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_CREATE_URI)
	public ResponseEntity<?> createEquipmentManufacturer(@RequestBody ManufacturerModule manufacturerModule) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.createEquipmentManufacturer(manufacturerModule));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_UPDATE_URI)
	public ResponseEntity<?> updateEquipmentManufacturer(@RequestBody ManufacturerModule manufacturerModule) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.updateEquipmentManufacturer(manufacturerModule));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.MasterManufacturerInterfaceUri.MASTER_MANUFACTURE_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteEquipmentManufacturer(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(manufacturerService.deleteEquipmentManufacturer(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

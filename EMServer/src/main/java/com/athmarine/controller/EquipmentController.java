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

import com.athmarine.exception.EquipmentNotFoundException;
import com.athmarine.request.EquipmentModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EquipmentService;

@RestController
@RequestMapping(path = RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_BASE_URI)
public class EquipmentController {

	@Autowired
	EquipmentService equipmentService;

	@PostMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_CREATE_URI)
	public ResponseEntity<?> createEquipmentName(@RequestBody EquipmentModel equipmentModel) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(equipmentService.createEquipmentName(equipmentModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_UPDATE_URI)
	public ResponseEntity<?> updateEquipment(@RequestBody EquipmentModel equipmentModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(equipmentService.updateEquipment(equipmentModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_GET_ALL_URI)
	public ResponseEntity<?> getAllEquimentName() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(equipmentService.getAllEquimentName());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_GET_URI + "/{id}")
	public ResponseEntity<?> getEquipmentById(@PathVariable("id") int id) throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(equipmentService.findEquipmentNameById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIPMENT_GET_ALL_BY_CATEGORY + "/{id}")
	public ResponseEntity<?> getAllEquipmentByCategoryId(@PathVariable("id") int id) throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(equipmentService.getAllEquipmentByCategory(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.EquipmentInterfaceUri.EQUIMENT_CATEGORY_GET_URI + "/{id}")
	public ResponseEntity<?> deleteEquipmentName(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(equipmentService.deleteEquipmentName(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

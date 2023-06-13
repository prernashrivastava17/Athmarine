package com.athmarine.controller;

import javax.servlet.http.HttpServletRequest;
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

import com.athmarine.exception.ModuleNotFoundException;
import com.athmarine.request.MasterEquipmentCategoryModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MasterEquipmentCategoryService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_BASE_URI)
public class MasterEquipmentCategoryController {

	@Autowired
	MasterEquipmentCategoryService masterEquipmentCategoryService;

	// API TO INSERT DATA INTO Master Equipment Category TABLE

	@PostMapping(path = RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_ADD_URI)
	public ResponseEntity<BaseApiResponse> createMasterEquipmentCategory(HttpServletRequest request,
			@Valid @RequestBody MasterEquipmentCategoryModel masterEquipmentCategoryModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				masterEquipmentCategoryService.saveMasterEquipmentCategoryDetails(masterEquipmentCategoryModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** Get Master Equipment CategoryAccounts
	@GetMapping(RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_GET_URI + "/{id}")
	public ResponseEntity<?> getMasterEquipmentCategoryById(@PathVariable("id") int id) throws ModuleNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterEquipmentCategoryService.getMasterEquipmentCategoryData(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@GetMapping(RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_GET_ALL_URI)
	public ResponseEntity<?> getAllEquipmentCategory() throws ModuleNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterEquipmentCategoryService.getAllMasterEquipmentCategory());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@GetMapping(RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_GET_ALLS_URI)
	public ResponseEntity<?> getAllEquipmentCategoryNonVerified() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(masterEquipmentCategoryService.getAllEquipmentCategoryNonVerified());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_UPDATE_URI)
	public ResponseEntity<?> updateMasterEquipmentCategory(
			@RequestBody MasterEquipmentCategoryModel masterEquipmentCategoryModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				masterEquipmentCategoryService.updateMasterEquipmentCategory(masterEquipmentCategoryModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***API TO DELETE Master Equipment CategoryAccounts

	@DeleteMapping(RestMappingConstants.MasterEquipmentCategoryInterfaceUri.MASTER_EQUIMENT_CATEGORY_DELETE_URI
			+ "/{id}")
	public ResponseEntity<?> deleteEquipmentCategoryById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterEquipmentCategoryService.deleteEquipmentCategoryDetails(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

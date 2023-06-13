package com.athmarine.controller;

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
import com.athmarine.request.ModuleModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.ModuleService;

@RestController
@RequestMapping(RestMappingConstants.ModuleInterfaceUri.MODULE_BASE_URI)
public class ModuleController {

	@Autowired
	ModuleService moduleService;

	// *** API To Create Module
	@PostMapping(RestMappingConstants.ModuleInterfaceUri.MODULE_ADD_URI)
	public ResponseEntity<?> createModule(@Valid @RequestBody ModuleModel moduleModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(moduleService.createModule(moduleModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** API To Update Module
	@PutMapping(RestMappingConstants.ModuleInterfaceUri.MODULE_UPDATE__URI)
	public ResponseEntity<?> updateModule(@RequestBody ModuleModel moduleModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(moduleService.updateModuleData(moduleModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** Get Module Accounts
	@GetMapping(RestMappingConstants.ModuleInterfaceUri.GET_MODULE_URI + "/{id}")
	public ResponseEntity<?> getModuleById(@PathVariable("id") int id) throws ModuleNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(moduleService.getModuleData(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** API To Delete Module
	@DeleteMapping(RestMappingConstants.ModuleInterfaceUri.MODULE_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteModuleById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(moduleService.deleteModule(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

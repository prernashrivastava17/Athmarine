package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.DepartmentNameModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.DepartmentNameService;

@RestController
@RequestMapping(path = RestMappingConstants.DepartmentNameInterfacUri.DEPARTMENT_NAME_BASE_URI)
public class DepartmentNameController {

	@Autowired
	DepartmentNameService departmentNameService;

	@PostMapping(path = RestMappingConstants.DepartmentNameInterfacUri.DEPARTMENT_NAME_ADD_URI)
	public ResponseEntity<BaseApiResponse> createDepartmentName(@RequestBody DepartmentNameModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(departmentNameService.createDepartmentName(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(path = RestMappingConstants.DepartmentNameInterfacUri.DEPARTMENT_NAME_GET_URI)
	public ResponseEntity<BaseApiResponse> getAllVerifiedDepartmentName() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(departmentNameService.getAllVerifiedDeprtmentName());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(path = RestMappingConstants.DepartmentNameInterfacUri.DEPARTMENT_NAME_VERIFY_URI + "/{id}")
	public ResponseEntity<BaseApiResponse> verifyDepartmentName(@RequestBody DepartmentNameModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(departmentNameService.verifyDepartmentName(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

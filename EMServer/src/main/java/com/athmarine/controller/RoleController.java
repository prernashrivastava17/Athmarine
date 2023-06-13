package com.athmarine.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.RoleModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.RoleDetailsServiceImpl;

@EnableJpaRepositories("com.athmarine.repository")
@RestController
@RequestMapping(RestMappingConstants.RoleRequestUri.ROLE_BASE_URI)
public class RoleController {

	@Autowired
	RoleDetailsServiceImpl roleDetailsServiceImpl;

	// *** API To Create Role
	@PostMapping(RestMappingConstants.RoleRequestUri.ADD_ROLE_URI)
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleModel roleModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(roleDetailsServiceImpl.createRole(roleModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** API To Update Role
	@PutMapping(RestMappingConstants.RoleRequestUri.UPDATE_ROLE_URI)
	public ResponseEntity<?> updateRole(@RequestBody RoleModel role) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(roleDetailsServiceImpl.updateRoleData(role));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** API To get Role
	@GetMapping(RestMappingConstants.RoleRequestUri.GET_ROLE_URI + "/{id}")
	public ResponseEntity<?> getRoleById(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(roleDetailsServiceImpl.getRoleData(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** API To Delete Role
	@DeleteMapping(RestMappingConstants.RoleRequestUri.DELETE_ROLE_URI + "/{id}")
	public ResponseEntity<?> deleteRoleById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(roleDetailsServiceImpl.deleteRole(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}
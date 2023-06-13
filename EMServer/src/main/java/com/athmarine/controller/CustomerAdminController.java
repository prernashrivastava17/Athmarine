package com.athmarine.controller;

import javax.mail.MessagingException;

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

import com.athmarine.exception.AdminNotFoundException;
import com.athmarine.exception.AppException;
import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerAdminService;

@RestController
@RequestMapping(RestMappingConstants.CustomerAdminInterfaceUri.CUSTOMER_ADMIN_BASE_URI)
public class CustomerAdminController {

	@Autowired
	CustomerAdminService customerAdminService;

	@PostMapping(RestMappingConstants.CustomerAdminInterfaceUri.CUSTOMER_ADMIN_CREATE_URI)
	public ResponseEntity<?> createCustomerAdmin(@RequestBody UserModel userModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerAdminService.createCustomerAdmin(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.CustomerAdminInterfaceUri.CUSTOMER_ADMIN_UPDATE_URI)
	public ResponseEntity<?> updateCustomerAdmin(@RequestBody CustomerCompanyModel customerCompanyModel)
			throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerAdminService.updateCustomerAdmin(customerCompanyModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.CustomerAdminInterfaceUri.CUSTOMER_ADMIN_GET_URI + "/{id}")
	public ResponseEntity<?> getCustomerAdmin(@PathVariable("id") int id) throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerAdminService.findCustomerAdminById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.CustomerAdminInterfaceUri.CUSTOMER_ADMIN_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteCustomerAdmin(@PathVariable("id") int id) throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerAdminService.deleteCustomerAdmin(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

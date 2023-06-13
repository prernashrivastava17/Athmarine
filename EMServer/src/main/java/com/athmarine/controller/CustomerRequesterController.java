package com.athmarine.controller;

import java.util.List;

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

import com.athmarine.exception.AppException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.UserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerRequesterService;
import com.athmarine.service.UserDetailsServiceImpl;

@RestController
@RequestMapping(path = RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_BASE_URI)
public class CustomerRequesterController {

	@Autowired
	CustomerRequesterService customerRequesterService;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@PostMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_ADD_URI)
	public ResponseEntity<?> createCustomerRequester(@RequestBody List<UserModel> userModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.addRequester(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_UPDATE_URI)
	public ResponseEntity<?> updateCustomerRequester(@RequestBody UserModel userModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.updateVerfiedCustomerRequester(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_GET_ALL_REQUESTER_BY_COMPANY_ID_URI
			+ "/{id}")
	public ResponseEntity<?> getAllNonApproverCustomerRequesterByComapnyId(@PathVariable("id") int id)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.getAllRequesterByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_GET_ALL_REQUESTERS_BY_COMPANY_ID_NAVIGATION_URI
			+ "/{id}")
	public ResponseEntity<?> getAllCustomerRequestersByComapnyId(@PathVariable("id") int id)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.getAllRequestersByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_GET_URI + "/{id}")
	public ResponseEntity<?> getCustomerRequesterById(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.findCustomerRequesterById(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_PROFILE_GET_URI + "/{id}")
	public ResponseEntity<?> getRequestorProfileByRequestorId(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.getRequestorProfileByRequestorId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@DeleteMapping(RestMappingConstants.CustomerRequesterInterfaceUri.CUSTOMER_REQUESTER_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteVendorBidder(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerRequesterService.deleteCustomerRequester(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

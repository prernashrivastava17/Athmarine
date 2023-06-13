package com.athmarine.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

import com.athmarine.exception.CustomerApproverNotFoundException;
import com.athmarine.request.CustomerApproverModel;
import com.athmarine.request.CustomerApproverUserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerApproverService;

@RestController
@RequestMapping(path = RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_BASE_URI)
public class CustomerApproverController {

	@Autowired
	CustomerApproverService customerApproverService;

	// API TO INSERT DATA INTO CUSTOMER APPROVER TABLE

	@PostMapping(path = RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_ADD_URI)
	public ResponseEntity<BaseApiResponse> createCustomerApprover(HttpServletRequest request,
			@Valid @RequestBody List<CustomerApproverUserModel> customerApproverUserModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.saveCustomerApproverDetails(customerApproverUserModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// GET API TO FETCH CUSTOMER APPROVER DETAILS

	@GetMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_GET_URI + "/{id}")
	public ResponseEntity<?> getCustomerApproverById(@NotNull @PathVariable("id") int id)
			throws CustomerApproverNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.getCustomerApproverDetails(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	// GET API TO FETCH CUSTOMER APPROVER DETAILS

	@GetMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_GET_ALL_APPORVER_BY_HEAD_ID_URI + "/{id}")
	public ResponseEntity<?> getAllCustomerApproverByHeadId(@NotNull @PathVariable("id") int id)
			throws CustomerApproverNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.getAllApproverByHeadId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	
	@GetMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_GET_ALL_URI + "/{id}")
	public ResponseEntity<?> getAllCustomerApproverById(@NotNull @PathVariable("id") int id)
			throws CustomerApproverNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.getAllApproverByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** API To Update Customer Company

	@PutMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_UPDATE_URI)
	public ResponseEntity<?> updateCustomerApprover(@RequestBody CustomerApproverModel approverModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.updateCustomerApprover(approverModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***API TO DELETE Customer Approver DETAILS

	@DeleteMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteCustomerApproverById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerApproverService.deleteCustomerApprovereDetails(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

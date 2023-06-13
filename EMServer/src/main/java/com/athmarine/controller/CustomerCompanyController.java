package com.athmarine.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.ServiceRequestNotCompletedException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerCompanyService;
import com.athmarine.service.UserCustomerModel;

@RestController
@RequestMapping(path = RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerCompanyController {

	@Autowired
	CustomerCompanyService customerCompanyService;
	
	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	// API TO INSERT DATA INTO Customer Company TABLE

	@PostMapping(path = RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_ADD_URI)
	public ResponseEntity<BaseApiResponse> createCustomerCompany(@RequestBody UserCustomerModel userCustomerModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerCompanyService.createCustomerComapny(userCustomerModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// GET API TO FETCH Customer Company DETAILS

	@GetMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_GET_BUSINESS_INFO_URI + "/{id}")
	public ResponseEntity<?> getCustomerCompanyBuisnessInfoById(@PathVariable("id") int id)
			throws CustomerCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerCompanyService.customerCompanyData(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_GET_URI + "/{id}")
	public ResponseEntity<?> getCustomerCompanyById(@PathVariable("id") int id)
			throws CustomerCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerCompanyService.getCustomerCompany(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** API To Update Customer Company

	@PutMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_UPDATE_URI)
	public ResponseEntity<?> updateCustomerCompany(@RequestBody CustomerCompanyModel customerApproverModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerCompanyService.updateCustomerCompany(customerApproverModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***API TO DELETE Customer Company DETAILS

	@DeleteMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_COMPANY_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteCustomerCompany(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerCompanyService.deleteCustomerCompany(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@GetMapping(RestMappingConstants.CustomerCompanyInterfaceUri.CUSTOMER_VALIDATE_REFERRAL_CODE_URI + "/{referralCode}")
	public ResponseEntity<?> validateReferralCode(@PathVariable("referralCode") String referralCode)
			throws ServiceRequestNotCompletedException {
		BaseApiResponse baseApiResponse=null;


		if (customerCompanyRepository.existsByReferralCode(referralCode)) {
			baseApiResponse = ResponseBuilder.getSuccessResponse();
		} else {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.REFERRAL_CODE_INCORRECT_ERROR,
					AppConstant.ErrorCodes.WRONG_REFERRAL_CODE_ERROR_CODE,
					AppConstant.ErrorMessages.WRONG_REFERRAL_CODE_MESSAGE);
		}

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

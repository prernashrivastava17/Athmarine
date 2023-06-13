package com.athmarine.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.UserFinanceTurnoverModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.CustomerFinanceService;

@RestController
@RequestMapping(path = RestMappingConstants.CustomerFinanceInterfaceUri.CUSTOMER_FINANCE_BASE_URI)
public class CustomerFinanceController {

	@Autowired
	CustomerFinanceService customerFinanceService;

	@PostMapping(RestMappingConstants.CustomerFinanceInterfaceUri.CUSTOMER_FINANCE_ADD_URI)
	public ResponseEntity<?> createCustomerFinance(@RequestBody List<UserFinanceTurnoverModel> userFinanceTurnoverModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerFinanceService.saveCustomerFinance(userFinanceTurnoverModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	@GetMapping(RestMappingConstants.CustomerFinanceInterfaceUri.CUSTOMER_COMPANY_GET_URI + "/{id}")
	public ResponseEntity<?> getFinanaceById(@NotNull @PathVariable("id") int id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerFinanceService.getAllFinanceByCompnayId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.CustomerFinanceInterfaceUri.CUSTOMER_FINANCE_ADD_URI)
	public ResponseEntity<?> updateCustomerFinance(@RequestBody UserFinanceTurnoverModel userFinanceTurnoverModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(customerFinanceService.updatesCustomerFinance(userFinanceTurnoverModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
}

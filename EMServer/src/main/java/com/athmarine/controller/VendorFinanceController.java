package com.athmarine.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.FinanceModel;
import com.athmarine.request.UserFinanceTurnoverModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorFinanceService;

@RestController
@RequestMapping(path = RestMappingConstants.FinanceInterfaceUri.FINANCE_BASE_URI)
public class VendorFinanceController {

	@Autowired
	VendorFinanceService financeService;

	// API TO INSERT DATA INTO FINANCE TABLE

	@PostMapping(path = RestMappingConstants.FinanceInterfaceUri.FINANCE_ADD_URI)
	public ResponseEntity<BaseApiResponse> createVendorFinance(HttpServletRequest request,
			@Valid @RequestBody List<UserFinanceTurnoverModel> userFinanceTurnoverModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.saveVendorFinance(userFinanceTurnoverModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// GET API TO FETCH FINANCE DETAILS

	@GetMapping(RestMappingConstants.FinanceInterfaceUri.FINANCE_GET_URI + "/{id}")
	public ResponseEntity<?> getFinanaceById(@NotNull @PathVariable("id") int id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.getAllFinanceByCompnayId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// API TO UPDATE FINANCE DETAILS

	@PutMapping(RestMappingConstants.FinanceInterfaceUri.FINANCE_UPDATE_URI)
	public ResponseEntity<?> updateFinanace(@Valid @RequestBody FinanceModel finanaceModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.updateFinanceDetails(finanaceModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***API TO DELETE VENDOR FINANCE DETAILS

	@DeleteMapping(RestMappingConstants.FinanceInterfaceUri.FINANCE_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteFinanceById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(financeService.deleteFinanceDetails(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	//---------GET API to get counts for Finance Dashboard---------//

	@GetMapping(RestMappingConstants.FinanceInterfaceUri.FINANCE_GET_COUNTS)
	public ResponseEntity<?> getAllCountsByStatus(@NotNull @RequestParam int financeId) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.getAllCountsByStatus(financeId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//---------GET API to get All Invoices By Status---------//

	@GetMapping(RestMappingConstants.FinanceInterfaceUri.GET_INVOICES_RAISED)
	public ResponseEntity<?> getAllInvoicesByStatus(@NotNull @RequestParam int financeId, String invoiceStatus, Integer page) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.getAllInvoicesByStatus(financeId,invoiceStatus, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//---------GET API to get all Payable Invoices---------//

	@GetMapping(RestMappingConstants.FinanceInterfaceUri.GET_PAYABLES)
	public ResponseEntity<?> getAllPayables(@NotNull @RequestParam int financeId, Integer page) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(financeService.getPayableInvoices(financeId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

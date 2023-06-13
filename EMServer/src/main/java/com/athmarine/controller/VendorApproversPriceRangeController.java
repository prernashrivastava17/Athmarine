package com.athmarine.controller;

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

import com.athmarine.request.VendorApproversPriceRangeModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorApproversPriceRangeService;

@RestController
@RequestMapping(path = RestMappingConstants.VendorApproversPriceRangeInterfaceUri.VENDOR_APPROVERS_PRICE_RANGE_BASE_URI)
public class VendorApproversPriceRangeController {

	@Autowired
	VendorApproversPriceRangeService vendorApproversPriceRangeService;

	// API TO INSERT DATA INTO Vendor Approvers PriceRange TABLE

	@PostMapping(path = RestMappingConstants.VendorApproversPriceRangeInterfaceUri.VENDOR_APPROVERS_PRICE_RANGE_ADD_URI)
	public ResponseEntity<BaseApiResponse> createVendorApproversPriceRange(HttpServletRequest request,
			@Valid @RequestBody VendorApproversPriceRangeModel vendorApproversPriceRangeModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				vendorApproversPriceRangeService.saveVendorApproversPriceRangeDetails(vendorApproversPriceRangeModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// GET API TO FETCH Vendor APPROVER Price Range DETAILS

	@GetMapping(RestMappingConstants.VendorApproversPriceRangeInterfaceUri.VENDOR_APPROVERS_PRICE_RANGE_GET_URI
			+ "/{id}")
	public ResponseEntity<?> getVendorApproversPriceRangeById(@NotNull @PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproversPriceRangeService.getVendorApproversPriceRangeDetails(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** API To Update Vendor Approver Price Range

	@PutMapping(RestMappingConstants.VendorApproversPriceRangeInterfaceUri.VENDOR_APPROVERS_PRICE_RANGE_UPDATE_URI)
	public ResponseEntity<?> updateVendorApproversPriceRange(
			@RequestBody VendorApproversPriceRangeModel vendorApproversPriceRangeModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				vendorApproversPriceRangeService.updateVendorApproversPriceRange(vendorApproversPriceRangeModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	// ***API TO DELETE Vendor Approver Price Range DETAILS

	@DeleteMapping(RestMappingConstants.VendorApproversPriceRangeInterfaceUri.VENDOR_APPROVERS_PRICE_RANGE_DELETE_URI
			+ "/{id}")
	public ResponseEntity<?> deleteVendorApproversPriceRangeById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproversPriceRangeService.deleteVendorApproversPriceRangeDetails(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

}

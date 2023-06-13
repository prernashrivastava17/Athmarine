
package com.athmarine.controller;

import java.util.List;

import javax.mail.MessagingException;

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

import com.athmarine.exception.AppException;
import com.athmarine.exception.VendorBidderNotFoundException;
import com.athmarine.request.UserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorBidderService;

@RestController
@RequestMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BASE_URI)
public class VendorBidderController {

	@Autowired
	VendorBidderService vendorBidderService;

	// *** API To Create Vendor Engineer

	@PostMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_ADD_BIDDER_URI)
	public ResponseEntity<?> createVendorBidder(@RequestBody List<UserModel> userModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.addVendorBidder(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BIDDER_UPDATE_ALL_URI)
	public ResponseEntity<?> updateAllVendorBidder(@RequestBody List<UserModel> userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.updateAllVendorBidder(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BIDDER_UPDATE_URI)
	public ResponseEntity<?> updateVendorBidder(@RequestBody UserModel userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.updateVendorBidder(userModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BIDDER_GET_ALL_URI + "/{id}")
	public ResponseEntity<?> getAllVendorBidder(@PathVariable("id") int id) throws VendorBidderNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.getAllBidderByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BIDDER_GET_ALL_BY_APPROVER_ID_URI
			+ "/{id}")
	public ResponseEntity<?> getAllVendorBidderByApproverId(@PathVariable("id") int id)
			throws VendorBidderNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.getAllBidderByAproverId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorBidderInterfaceUri.VENDOR_COMPANY_BIDDER_GET_URI + "/{id}")
	public ResponseEntity<?> getVendorBidder(@PathVariable("id") int id) throws VendorBidderNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.getAllBidderByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorBidderInterfaceUri.GET_ALL_COMPANY_VENDOR_BIDDER_URI + "/{id}")
	public ResponseEntity<?> getAllVendorCompanyBidder(@PathVariable("id") int id)
			throws VendorBidderNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorBidderService.getAllBidderByCMPId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	/*
	 * @DeleteMapping(RestMappingConstants.VendorBidderInterfaceUri.
	 * VENDOR_COMPANY_BIDDER_DELETE_URI + "/{id}") public ResponseEntity<?>
	 * deleteVendorBidder(@PathVariable("id") int id) throws
	 * VendorBidderNotFoundException {
	 * 
	 * BaseApiResponse baseApiResponse = ResponseBuilder
	 * .getSuccessResponse(vendorBidderService.deleteVendorBidder(id));
	 * 
	 * return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK); }
	 */
}

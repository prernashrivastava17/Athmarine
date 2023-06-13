package com.athmarine.controller;

import java.util.List;

import javax.mail.MessagingException;

import com.athmarine.request.BidsModel;
import com.athmarine.request.VendorApproverBidRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.exception.AppException;
import com.athmarine.exception.VendorEngineerNotFoundException;
import com.athmarine.request.VendorApproverModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorApproverService;

@RestController
@RequestMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_COMPANY_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorApproverController {

	@Autowired
	VendorApproverService vendorApproverService;

// *** API To Create Vendor Approver

	@PostMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_APPROVER_CREATE_URI)
	public ResponseEntity<?> createVendorApprover(@RequestBody List<VendorApproverModel> vendorApproverModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.createVendorApprover(vendorApproverModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_APPROVER_UPDATE_URI)
	public ResponseEntity<?> updateAllVendorBidder(@RequestBody List<VendorApproverModel> vendorApproverModel)
			throws AppException, MessagingException {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.updateVendorApprover(vendorApproverModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_APPROVER_GET_URI + "/{id}")
	public ResponseEntity<?> getVendorAllApproverByCompanyId(@PathVariable("id") int id) throws VendorEngineerNotFoundException {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.getVendorAllApproverByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//----------GET API to get all counts for dashboard----------//

	@GetMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_GET_COUNTS)
	public ResponseEntity<?> getAllCounts(@RequestParam Integer approverId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.getCounts(approverId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all approved bids----------//

	@GetMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_GET_APPROVED_BIDS)
	public ResponseEntity<?> getAllApprovedBids(@RequestParam Integer approverId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.getAllApprovedBids(approverId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all to-approve bids----------//

	@GetMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_GET_TO_APPROVED_BIDS)
	public ResponseEntity<?> getAllToApproveBids(@RequestParam Integer approverId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorApproverService.getAllToApproveBids(approverId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//---------PUT API to approve or decline the bid---------//

	@PutMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_APPROVE_OR_DECLINE_BID)
	public ResponseEntity<?> approveOrDeclineBid(@RequestBody VendorApproverBidRequestModel approverBidRequestModel){
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vendorApproverService
				.approveOrDeclineBid(approverBidRequestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//---------PUT API to update bid by approver---------//

	@PutMapping(RestMappingConstants.VendorApproverInterfaceUri.VENDOR_APPROVE_UPDATE_BID)
	public ResponseEntity<?> updateBidByApprover(@RequestBody BidsModel bidsModel) throws Exception {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vendorApproverService
				.updateBidByApprover(bidsModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}
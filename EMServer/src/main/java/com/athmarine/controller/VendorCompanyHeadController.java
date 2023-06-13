package com.athmarine.controller;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

import com.athmarine.request.VendorApproverBidRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.exception.AppException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.HocRequestModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorCompanyHeadService;

@RestController
@RequestMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorCompanyHeadController {

	@Autowired
	VendorCompanyHeadService vendorCompanyHeadService;

	@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_CREATE_URI)
	public ResponseEntity<?> createVendorCompanyHead(@RequestBody UserModel headModel)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.createVendorCompanyHead(headModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_UPDATE_URI)
	public ResponseEntity<?> updateVendorCompanyHead(@RequestBody UserModel userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.updateVendorComapnyHead(userModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_URI + "/{id}")
	public ResponseEntity<?> getVendorCompanyById(@PathVariable("id") int id) throws ResourceNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.findCompanyHead(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// --------Get All Counts-------------
	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_ALL_COUNT_URI)
	public ResponseEntity<?> getAllCounts(@RequestParam Integer headId) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllCounts(headId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// --------API to get all PO's ----------------------
	@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_PO_RECEIVED_URI)
	public ResponseEntity<?> getAllPO(@RequestBody HocRequestModel requestModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllPOBystatus(requestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	//-----------API to get all raised bid -------------------
	@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_RAISED_BIDS_URI)
	public ResponseEntity<?> getAllRaisedBids(@RequestBody HocRequestModel requestModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllRaisedBidsByStatus(requestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	//----------API to get Interested Request -----------
	@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_INTERESTED_REQUEST_URI)
	public ResponseEntity<?> getAllInterestedRequest(@RequestBody HocRequestModel requestModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllInterestedRequestByStatus(requestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	//----------API to get all paid invoice -----------
		@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_INVOICE_PAID_URI)
		public ResponseEntity<?> getAllInvoicePaid(@RequestBody HocRequestModel requestModel) {

			BaseApiResponse baseApiResponse = ResponseBuilder
					.getSuccessResponse(vendorCompanyHeadService.getAllInvoicePaid(requestModel));
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
		}
		
		//----------API to get all raised invoice -----------
		@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_INVOICE_RAISED_URI)
		public ResponseEntity<?> getAllRaisedInvoice(@RequestBody HocRequestModel requestModel) {

					BaseApiResponse baseApiResponse = ResponseBuilder
							.getSuccessResponse(vendorCompanyHeadService.getAllRaisedInvoice(requestModel));
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
		}
		
		//----------API to get all raised invoice -----------
		@PostMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_COMPLETED_JOB_URI)
		public ResponseEntity<?> getAllCompetedJobs(@RequestBody HocRequestModel requestModel) {

					BaseApiResponse baseApiResponse = ResponseBuilder
							.getSuccessResponse(vendorCompanyHeadService.getAllCompetedJobs(requestModel));
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
		}

	// --------Get All Counts-------------
	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_ALL_USERS_URI)
	public ResponseEntity<?>  getAllUsers(@RequestParam @NotNull Integer headId ,@RequestParam @NotNull Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllUsers(headId,page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	//-------------API to get all bids whose status is approval pending--------------
	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_All_BIDS_FOR_APPROVAL_URI)
	public ResponseEntity<?>  getAllBidsByHeadId(@RequestParam @NotNull Integer headId,@RequestParam @NotNull Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllBidsByHeadId(headId,page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_UPDATE_BID_STATUS_URI)
	public ResponseEntity<?> updateBidStatus(@RequestBody VendorApproverBidRequestModel requestModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.updateBidStatus(requestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_UPDATE_BID_BY_HOC_URI)
	public ResponseEntity<?> updateBidByHoC(@RequestBody VendorApproverBidRequestModel model) throws Exception {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.updateBidByHoC(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	// --------Api to get all access request for user -------------
	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_ALL_USERS_FOR_ACCESS_REQUEST_URI)
	public ResponseEntity<?>  getAllUsersforAccessRequestByHeadId(@RequestParam @NotNull Integer headId,@RequestParam @NotNull Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllUsersforAccessRequestByHeadId(headId,page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	// --------Api to get all access request for user -------------
	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_CHANGE_ACCESS_REQUEST_STATUS_URI)
	public ResponseEntity<?>  ChangeStatusOfAccessibility(@RequestBody HocRequestModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.ChangeStatusOfAccessibility(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// --------GET API to Get All New Users For Approval------------//

	@GetMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_GET_ALL_NEW_USERS_FOR_APPROVAL)
	public ResponseEntity<?>  getAllNewUsersForApproval(@RequestParam @NotNull Integer headId,@RequestParam @NotNull Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.getAllNewUsersForApproval(headId,page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	// --------PUT API to Change New User or User Update Approval Status------------//

	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_CHANGE_USER_APPROVAL_STATUS)
	public ResponseEntity<?>  changeUserApprovalStatus(@RequestBody HocRequestModel requestModel) throws InstantiationException, IllegalAccessException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.changeStatusOfApproval(requestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// --------PUT API to Update a User's Access Status------------//

	@PutMapping(RestMappingConstants.VendorCompanyHeadInterfaceUri.VENDOR_HEAD_UPDATE_USER_ACCESS)
	public ResponseEntity<?>  updateUserAccess(@RequestParam @NotNull Integer userId,@RequestParam @NotNull String status) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyHeadService.updateUserAccess(userId,status));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}

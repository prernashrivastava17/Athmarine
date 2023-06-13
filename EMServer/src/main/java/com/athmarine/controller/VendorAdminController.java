package com.athmarine.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.exception.AdminNotFoundException;
import com.athmarine.exception.AppException;
import com.athmarine.request.UserModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorAdminService;

@RestController
@RequestMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorAdminController {

	@Autowired
	VendorAdminService vendorAdminService;

	@PostMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_CREATE_URI)
	public ResponseEntity<?> createVendForAdmin(@RequestBody UserModel userModel)

			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.createVendorAdmin(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_UPDATE_URI)
	public ResponseEntity<?> updateVendorAdmin(@RequestBody UserModel model) throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.updateVendorAdmin(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_URI + "/{id}")
	public ResponseEntity<?> getVendorAdmin(@PathVariable("id") int id) throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.findVendorAdminById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteVendorAdmin(@PathVariable("id") int id) throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(vendorAdminService.deleteVendorAdmin(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//----------GET API to get all counts for dashboard----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_COUNTS)
	public ResponseEntity<?> getAllCounts(@RequestParam Integer adminId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllCounts(adminId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all Active Users----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_USERS)
	public ResponseEntity<?> getAllActiveUsers(@RequestParam Integer adminId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllActiveUsers(adminId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all Active Bidders----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_BIDDERS)
	public ResponseEntity<?> getAllActiveBidders(@RequestParam Integer adminId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllActiveBidders(adminId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all Active Approvers----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_APPROVER)
	public ResponseEntity<?> getAllActiveApprovers(@RequestParam Integer adminId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllActiveApprovers(adminId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all Active Engineers----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_ENGINEERS)
	public ResponseEntity<?> getAllActiveEngineers(@RequestParam Integer adminId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllActiveEngineers(adminId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get all Active Financers----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_FINANCERS)
	public ResponseEntity<?> getAllActiveFinancers(@RequestParam Integer adminId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getAllActiveFinancers(adminId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get HOC Details----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_ACTIVE_HOC)
	public ResponseEntity<?> getActiveHOC(@RequestParam Integer adminId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getActiveHOC(adminId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//----------PUT API to update user access----------//

	@PutMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_UPDATE_USER_ACCESS)
	public ResponseEntity<?> updateUserAccess(@RequestParam Integer userId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.updateUserAccess(userId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------POST API to create New User----------//

	@PostMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_CREATE_NEW_USER)
	public ResponseEntity<?> createNewUser(@RequestBody UserModel userModel){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.createNewUser(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------POST API to create New HOC----------//

	@PostMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_CREATE_NEW_HOC)
	public ResponseEntity<?> createNewVendorHead(@RequestBody UserModel userModel){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.createNewVendorHead(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------PUT API to update user----------//

	@PutMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_UPDATE_USER)
	public ResponseEntity<?> updateUser(@RequestBody UserModel userModel){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.updateUser(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get Updated Details----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_UPDATED_DETAILS)
	public ResponseEntity<?> getUserUpdatedDetails(@RequestParam Integer userId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getUserUpdatedDetails(userId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get User Updated Decline Reason----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_UPDATE_DECLINE_REASON)
	public ResponseEntity<?> getUserUpdateDeclineReason(@RequestParam Integer userId){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getUpdateDeclineReason(userId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//----------GET API to get User By Id and Role----------//

	@GetMapping(RestMappingConstants.VendorAdminInterfaceUri.VENDOR_ADMIN_GET_USER_BY_ID)
	public ResponseEntity<?> getUserByIdAndRole(@RequestParam Integer userId, String role){
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorAdminService.getUserByIdAndRole(userId,role));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}
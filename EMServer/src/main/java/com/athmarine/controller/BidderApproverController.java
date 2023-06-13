package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.service.BidderApproverService;

@RestController
@RequestMapping(path = RestMappingConstants.BidderApproverInterfaceUri.BIDDER_APPROVER_BASE_URI)
public class BidderApproverController {
	
	@Autowired
	BidderApproverService bidderApproverService;
	
	// API TO INSERT DATA INTO BIDDER APPROVER TABLE
/*
		@PostMapping(path = RestMappingConstants.BidderApproverInterfaceUri.BIDDER_APPROVER_ADD_URI)
		public ResponseEntity<BaseApiResponse> createBidderApprover(HttpServletRequest request,
				@Valid @RequestBody BidderApproverModel bidderApproverModel) {

			BaseApiResponse baseApiResponse = ResponseBuilder
					.getSuccessResponse(bidderApproverService.saveBidderrApproverDetails(bidderApproverModel));
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

		}
	
		// GET API TO FETCH BIDDER APPROVER DETAILS

		@GetMapping(RestMappingConstants.BidderApproverInterfaceUri.BIDDER_APPROVER_GET_URI + "/{id}")
		public ResponseEntity<?> getBidderApproverById(@NotNull @PathVariable("id") int id) {

			BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidderApproverService.getBidderApproverDetails(id));

			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

		}
		
		// *** API To Update Bidder Approver

		@PutMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_UPDATE_URI)
		public ResponseEntity<?> updateCustomerApprover(@RequestBody BidderApproverModel bidderApproverModel) {

			BaseApiResponse baseApiResponse = ResponseBuilder
					.getSuccessResponse(bidderApproverService.updateBidderApprover(bidderApproverModel));

			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
		}
		
		// ***API TO DELETE BIDDER Approver DETAILS

		@DeleteMapping(RestMappingConstants.CustomerApproverInterfaceUri.CUSTOMER_APPROVER_DELETE_URI + "/{id}")
		public ResponseEntity<?> deleteBidderApproverById(@PathVariable @NotBlank int id) {

			BaseApiResponse baseApiResponse = ResponseBuilder
					.getSuccessResponse(bidderApproverService.deleteBidderApprovereDetails(id));
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

		}
		*/

}

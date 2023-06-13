package com.athmarine.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.athmarine.entity.ServiceRequestStatus;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.exception.AppException;
import com.athmarine.exception.BidsNotFoundException;
import com.athmarine.request.BidderRequestRelationModel;
import com.athmarine.request.BidsModel;
import com.athmarine.request.EngineerAllocationModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.InvoiceResponseModel;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.BidsService;

import java.sql.Date;

@RestController
@RequestMapping(path = RestMappingConstants.BidsInterfaceUri.BIDS_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BidsController {

	@Autowired
	public BidsService bidsService;

	//------------POST Api to Create Bid----------------//

	@PostMapping(RestMappingConstants.BidsInterfaceUri.BIDS_SUBMIT_URI)
	public ResponseEntity<?> submitBid(@RequestBody BidsModel bidsModel) throws AppException, Exception {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.createBids(bidsModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	//------------GET API To Get Bidder's All Bids by Status------------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_ALL_VENDOR_BIDS)
	public ResponseEntity<?> getAllBidsByStatus(@RequestParam Integer bidderId, ServiceRequestStatus status, Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				bidsService.getAllVendorBidsByStatus(bidderId, status, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ------------GET API To Get Bid by Id------------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.BID_BY_ID)
	public ResponseEntity<?> getBidById(@RequestParam Integer bidId) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getBidById(bidId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// -----------PUT API to Withdraw Bids In Progress-----------------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.WITHDRAW_BID)
	public ResponseEntity<?> withdrawBid(@RequestParam Integer bidId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.withdrawBid(bidId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// -----------PUT API to Raise PO-------------------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.RAISE_PO)
	public ResponseEntity<?> raisePO(@RequestParam Integer bidId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.raisePO(bidId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ------------PUT API to accept PO--------------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.ACCEPT_PO)
	public ResponseEntity<?> acceptPO(@RequestParam Integer poId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.acceptPO(poId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ------------PUT API to decline PO--------------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.DECLINE_PO)
	public ResponseEntity<?> declinePO(@RequestParam Integer poId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.declinePO(poId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------GET API to get All Raised POs By Bidder Id------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_ALL_RAISED_POs)
	public ResponseEntity<?> getAllRaisedPOs(@RequestParam Integer bidderId, Integer page){
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				bidsService.getAllRaisedPOs(bidderId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ---------GET API to get PO by PO id----------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_PO_BY_ID)
	public ResponseEntity<?> getPOById(@RequestParam Integer poId) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getPOById(poId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//---------GET API to get All Jobs--------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_ALL_JOBS_BY_BIDDERID)
	public ResponseEntity<?> getAllJobs(@RequestParam Integer bidderId, String jobStatus, Integer page){

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getAllJobs(bidderId,
				jobStatus, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// --------PUT API to Withdraw PO-------------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.WITHDRAW_PO)
	public ResponseEntity<?> withdrawPO(@RequestParam Integer poId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.withdrawPO(poId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//GET API to get all Engineers with ranking by company id
	@GetMapping(RestMappingConstants.BidsInterfaceUri.BIDS_GET_ENGINEERURI)
	public ResponseEntity<?> getEngineersRankingByCompanyId(@RequestParam int id, @RequestParam int requestId)
			throws BidsNotFoundException {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getEngineer(id,requestId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	// GET API TO Get engineers details for allocation
	@GetMapping(RestMappingConstants.BidsInterfaceUri.BIDS_GET_ENGINEER_ALLOCATON_DETAILS_URI + "/{id}")
	public ResponseEntity<?> getAllBidRequestDetails(@NotNull @PathVariable("id") int bidderId) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(bidsService.getAllBidRequestEngineerDetails(bidderId));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.BidsInterfaceUri.BIDS_ALLOCATE_ENGINEERS)
	public ResponseEntity<?> engineerAllocation(@RequestBody EngineerAllocationModel engineerAllocationModel)
			throws AppException, Exception {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(bidsService.allocateEngineer(engineerAllocationModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}


	// *** API To Update bid from interested to not interested
	@PutMapping(RestMappingConstants.BidsInterfaceUri.BIDS_UPDATE_STATUS_URI)
	public ResponseEntity<?> updateInterestedToNotInterested(@RequestBody BidderRequestRelationModel model)
			throws Exception {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(bidsService.updateInterestedToNotInterested(model));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	

	// *** API To Update Submit Bid
	@PutMapping(RestMappingConstants.BidsInterfaceUri.BIDS_UPDATE_BID_URI)
	public ResponseEntity<?> updatedSubmitBid(@RequestBody BidsModel bidsModel) throws Exception {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.updateSubmitedBid(bidsModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------PUT API to Raise Additional Cost-----------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.RAISE_ADD_COST)
	public ResponseEntity<?> raiseAdditionalCost(@RequestBody BidsModel bidsModel) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				bidsService.updateBidsAdditionalCost(bidsModel, ServiceRequestStatus.RAISED.name()));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ---------GET API to Get All Raised Additional Cost Bids-----------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_ALL_RAISED_ADDITIONAL_COST_BIDS)
	public ResponseEntity<?> getAllAdditionalCostRaisedBids(@RequestParam Integer bidderId, Integer page){

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getAllAdditionalCostRaisedBids(bidderId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------PUT API to Revoke Raised Additional Cost-----------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.REVOKE_ADD_COST)
	public ResponseEntity<?> revokeAdditionalCost(@RequestParam Integer bidId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.revokeAdditionalCost(bidId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------PUT API to Receive Additional Cost-----------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.RECEIVE_ADD_COST)
	public ResponseEntity<?> receiveAdditionalCost(@RequestBody BidsModel bidsModel) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				bidsService.updateBidsAdditionalCost(bidsModel, ServiceRequestStatus.RECEIVED.name()));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------GET API to Get Invoice Details-----------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_INVOICE_DETAILS)
	public ResponseEntity<?> getInvoiceDetails(@RequestParam Integer invoiceId) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getInvoiceDetails(invoiceId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ----------PUT API to Raise Invoice -----------//

	@PutMapping(RestMappingConstants.BidsInterfaceUri.RAISE_INVOICE)
	public ResponseEntity<?> raiseInvoice(@RequestParam Integer invoiceId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.raiseInvoice(invoiceId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.BidsInterfaceUri.DOWNLOAD_INVOICE)
	public ResponseEntity<?> downloadInvoiceDetail(@RequestBody InvoiceResponseModel model,
			HttpServletResponse response) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(bidsService.generatePdfForInovoice(model, response));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//--------GET API to get All Withdrawn POs------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_WITHDRAWN_POs)
	public ResponseEntity<?> getAllWithdrawnPOs(@RequestParam Integer bidderId, Integer page) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getAllWithdrawnPOs(bidderId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//--------GET API to get All Bids Live Status------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_LIVE_STATUS)
	public ResponseEntity<?> getLiveStatus(@RequestParam Integer bidderId, Integer page) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getAllLiveStatus(bidderId, page));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//--------GET API to get All History Counts------------//

	@GetMapping(RestMappingConstants.BidsInterfaceUri.GET_HISTORY_COUNTS)
	public ResponseEntity<?> getHistoryCounts(@RequestParam @NotNull Integer bidderId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getHistoryCounts(bidderId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	//-------------------Get All Engineers With Job Details---------------------
	@GetMapping(RestMappingConstants.BidsInterfaceUri.BIDS_GET_ALL_ENGINEER_WITH_JOBS_URI)
	public ResponseEntity<?> getAllEngineerJobDetails(@RequestParam @NotNull Integer bidderId, @RequestParam @NotNull Integer month) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getAllEngineerJobDetails(bidderId,month));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	//-------------------Get All Engineers With Job Details---------------------
	@GetMapping(RestMappingConstants.BidsInterfaceUri.BIDS_GET_ENGINEER_JOB_DETAILS_URI)
	public ResponseEntity<?> getEngineerJobDetail(@RequestParam @NotNull Integer engineerId, @RequestParam @NotNull String serviceDate) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(bidsService.getEngineerJobDetail(engineerId,serviceDate));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	//---------------downloadPOByPOID----------------------------------------
	@GetMapping(RestMappingConstants.BidsInterfaceUri.DOWNLOAD_PO)
	public ResponseEntity<?> downloadPOByPOID(@RequestParam @NotNull Integer PoId,
												   HttpServletResponse response) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(bidsService.downloadPOByPOID(PoId, response));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}

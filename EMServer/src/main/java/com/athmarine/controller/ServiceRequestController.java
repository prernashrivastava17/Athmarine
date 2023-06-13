package com.athmarine.controller;

import com.athmarine.entity.ServiceRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athmarine.request.ServiceRequestModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.ServiceRequestService;

@RestController
@RequestMapping(RestMappingConstants.ServiceRequestInterfaceUri.SERVICE_REQUEST_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceRequestController {

	@Autowired
	ServiceRequestService serviceRequestService;

	//-------------POST API To Create ServiceRequest------------------//

	@PostMapping(RestMappingConstants.ServiceRequestInterfaceUri.SERVICE_REQUEST_CREATE_URI)
	public ResponseEntity<?> createServiceRequest(@RequestBody ServiceRequestModel serviceRequestModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(serviceRequestService.createServiceRequest(serviceRequestModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//--------------GET API to fetch all Requests By Status----------------//

	@GetMapping(RestMappingConstants.ServiceRequestInterfaceUri.SERVICE_REQUESTS_BY_STATUS)
	public ResponseEntity<?> getAllServiceRequestsByStatus(@RequestParam ServiceRequestStatus serviceRequestStatus,
														   Integer bidderId, Integer page)
	{
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				serviceRequestService.getAllServiceRequestsByStatus(serviceRequestStatus, bidderId, page));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//-------------GET API to get Request By Id-----------------//

	@GetMapping(RestMappingConstants.ServiceRequestInterfaceUri.SERVICE_REQUEST_BY_ID+"/{id}")
	public ResponseEntity<?> getServiceRequestByID(@PathVariable("id") Integer id)
	{
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				serviceRequestService.getServiceRequestById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


	//-----------PUT API to update Request Status to Interested or Not Interested-------------//

	@PutMapping(RestMappingConstants.ServiceRequestInterfaceUri.CHANGE_REQUEST_STATUS)
	public ResponseEntity<?> changeRequestStatusByBidderId(@RequestParam Integer bidderId,
														   Integer serviceRequestId, String requestStatus){

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				serviceRequestService.changeRequestStatusByBidderId(serviceRequestId,bidderId,requestStatus));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//-------------GET API to get All Interested Or Not-Interested Requests By Bidder Id-----------------//

	@GetMapping(RestMappingConstants.ServiceRequestInterfaceUri.GET_BIDDER_REQUESTS_BY_STATUS)
	public ResponseEntity<?> getBiddersRequestsByStatus(@RequestParam Integer bidderId, String requestStatus, Integer page)
	{
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				serviceRequestService.getBiddersRequestsByStatus(bidderId, requestStatus, page));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	//-----------GET API to get counts of Requests According to their status by bidder_id----------//

	@GetMapping(RestMappingConstants.ServiceRequestInterfaceUri.GET_ALL_COUNTS + "/{id}")
	public ResponseEntity<?> getServiceRequestCountByBidderId(@PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(serviceRequestService.getServiceRequestsCounts(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.entity.BidEngineerRelation;
import com.athmarine.entity.ServiceRequestStatus;
import com.athmarine.request.BidEngineerRelationModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EngineerService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = RestMappingConstants.EngineerInterfaceUri.ENGINEER_JOB_DETAILS_BASE_URI)
public class EngineerController {
	
	@Autowired
	EngineerService engineerService;
	//--------Get All Counts-------------
	@GetMapping(RestMappingConstants.EngineerInterfaceUri.ENGINEER_JOB_DETAILS_GET_COUNTS_URI)
	public ResponseEntity<?> getAllCountsByEngineerId(@RequestParam @NotNull Integer engineerId) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				engineerService.getAllCountsByEngineerId(engineerId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	// ------------API to change the status of job assigned to in progress--------------
	@PutMapping(RestMappingConstants.EngineerInterfaceUri.ENGINEER_JOB_STATUS_CHANGE_URI)
	public ResponseEntity<?> updateStatusToInProgress(@RequestParam @NotNull Integer engineerId,@RequestParam @NotNull Integer serviceRequestId) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(engineerService.updateStatusToInProgress(engineerId,serviceRequestId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	//---------------API to store the refrence of uploaded Service report on AWS
	@PostMapping(RestMappingConstants.EngineerInterfaceUri.ENGINEER_JOB_SERVICE_REPORT_UPLOAD)
	public ResponseEntity<?> prepareServiceReport(@RequestBody BidEngineerRelationModel serviceReportDetails) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(engineerService.prepareServiceReport(serviceReportDetails));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}
	
	//-----------API to get All uploaded link of service Report and service request  ---------------
	@GetMapping(RestMappingConstants.EngineerInterfaceUri.GET_ENGINEER_JOB_SERVICE_REPORT)
	public ResponseEntity<?> getAllServiceReport(@RequestParam @NotNull Integer engineerId) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				engineerService.getAllServiceReport(engineerId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	//----------API to get all jobs according to status such as ASSIGNED OR JOB_IN_PROGRESS -------------
	@GetMapping(RestMappingConstants.EngineerInterfaceUri.ENGINEER_GET_ALL_JOB_BY_STATUS)
	public ResponseEntity<?> getAllJobsByStatus(@RequestParam @NotNull Integer engineerId ,@RequestParam @NotNull String status) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				engineerService.getAllJobsByStatus(engineerId,status));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	//-----------Get Calendar Details by engineer Id-----------------
	@GetMapping(RestMappingConstants.EngineerInterfaceUri.GET_ENGINEER_JOB_CALENDAR_DETAILS)
	public ResponseEntity<?> getEngineerJobCalendar(@RequestParam @NotNull Integer engineerId,
													@RequestParam @NotNull Integer month) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				engineerService.getEngineerJobCalendar(engineerId,month));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
}

package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.FeedbackService;

@RestController
@RequestMapping(RestMappingConstants.FeedBackInterfaceUri.FEEDBACK_BASE_URI)
public class FeedBackController {
	
	@Autowired
	FeedbackService feedbackService;
	
	@GetMapping(RestMappingConstants.FeedBackInterfaceUri.FEEDBACK_GET_VENDOR_COMAPANY_URI + "/{id}")
	public ResponseEntity<?> getVendorCompanyFeedback(@PathVariable("id") int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(feedbackService.getVendorCompanyFeedback(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

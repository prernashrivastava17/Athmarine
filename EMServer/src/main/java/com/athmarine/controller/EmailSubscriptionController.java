package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.EmailSubscriptionModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EmailSubscriptionService;

@RestController
@RequestMapping(path = RestMappingConstants.EmailSubscriptionInterfaceUri.EMAIL_SUBSCRIPTION_BASE_URI)
public class EmailSubscriptionController {

	@Autowired
	EmailSubscriptionService subscriptionService;

	@PostMapping(path = RestMappingConstants.EmailSubscriptionInterfaceUri.CREATE_EMAIL_SUBSCRIPTION_URI)
	public ResponseEntity<BaseApiResponse> createEmailSubscription(@RequestBody EmailSubscriptionModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(subscriptionService.createEmailSubscription(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.EmailSubscriptionInterfaceUri.GET_EMAIL_SUBSCRIPTION_URI)
	public ResponseEntity<?> getAllEmailSubscription() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(subscriptionService.getAllEmailSubscription());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

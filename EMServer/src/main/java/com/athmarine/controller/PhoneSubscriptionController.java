package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.PhoneSubscriptionModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.PhoneSubscriptionService;

@RestController
@RequestMapping(path = RestMappingConstants.PhoneSubscriptionInterfaceUri.PHONE_SUBSCRIPTION_BASE_URI)
public class PhoneSubscriptionController {

	@Autowired
	PhoneSubscriptionService phoneSubscriptionService;

	@PostMapping(path = RestMappingConstants.PhoneSubscriptionInterfaceUri.CREATE_PHONE_SUBSCRIPTION_URI)
	public ResponseEntity<BaseApiResponse> createPhoneSubscription(@RequestBody PhoneSubscriptionModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(phoneSubscriptionService.createPhoneSubscription(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping(RestMappingConstants.PhoneSubscriptionInterfaceUri.GET_PHONE_SUBSCRIPTION_URI)
	public ResponseEntity<?> getAllPhoneSubscription() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(phoneSubscriptionService.getAllPhoneSubscription());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

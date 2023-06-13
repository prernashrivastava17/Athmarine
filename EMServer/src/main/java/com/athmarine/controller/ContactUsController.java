package com.athmarine.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.AppException;
import com.athmarine.request.ContactUsModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.ContactUsService;

@RestController
@RequestMapping(path = RestMappingConstants.ContactUsInterfaceUri.CONTACT_US_BASE_URI)
public class ContactUsController {

	@Autowired
	ContactUsService contactUsService;

	@PostMapping(RestMappingConstants.ContactUsInterfaceUri.CONTACT_US_ADD_URI)
	public ResponseEntity<?> createContactUs(@RequestBody ContactUsModel model)
			throws AppException, MessagingException {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(contactUsService.createContactUs(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

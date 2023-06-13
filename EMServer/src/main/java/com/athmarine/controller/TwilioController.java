package com.athmarine.controller;

//package com.athmarine.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.athmarine.resources.RestMappingConstants;
//import com.athmarine.response.BaseApiResponse;
//import com.athmarine.response.ResponseBuilder;
//import com.athmarine.service.TwilioService;
//@RestController
//@RequestMapping(RestMappingConstants.TwilioInterfaceUri.TWILIO_BASE_URI)
//
//public class TwilioController {
//
//	@Autowired
//	TwilioService twilioService;
//	
//	@PostMapping(path = RestMappingConstants.TwilioInterfaceUri.TWILIO_MESSAGE_URI)
//	public ResponseEntity<BaseApiResponse> sendMessage(@RequestBody String phoneNumber, String sendOTPPhone) {
//		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(twilioService.sendMessage(phoneNumber,sendOTPPhone));
//		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
//	}
//
//}

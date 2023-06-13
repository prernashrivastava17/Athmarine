package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.TestModel;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.FeedbackService;
import com.athmarine.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	TestService testService;

	@Autowired
	FeedbackService testService2;

	@PostMapping("/AddUser")
	public ResponseEntity<?> createUser(@RequestBody TestModel userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(testService.createTest(userModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping("/getEnginerFeedback")
	public ResponseEntity<?> createUser(@RequestParam Integer id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(testService2.getEngineerFeedback(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@GetMapping("/testDatabaseConnection")
	public ResponseEntity<?> getAllEmailSubscription() {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(testService.testDatabaseConnection());
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}

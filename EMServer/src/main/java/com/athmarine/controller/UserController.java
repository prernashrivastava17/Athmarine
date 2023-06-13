package com.athmarine.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.request.EmailAndPhoneValidationRequest;
import com.athmarine.request.EmailRequestModel;
import com.athmarine.request.EmailTesting;
import com.athmarine.request.RequestorUpdateModel;
import com.athmarine.request.UserModel;
import com.athmarine.request.UserUpdateModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EmailService;
import com.athmarine.service.UserDetailsServiceImpl;

@RestController
@RequestMapping(RestMappingConstants.UserInterfaceUri.USER_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PreAuthorize("hasAnyAuthority('ADMIN','FINANCE')")
	@GetMapping("/restrictedAuth")
	public String authTest() {
		return "only authorized users can have access";
	}
	
	@Autowired
	public EmailService emailService;

	@PreAuthorize("permitAll()")
	@GetMapping("/allAuth")
	public @ResponseBody String allAuth() {

		return "works for all roles";
	}

	// *** Create User Accounts
	@PostMapping(RestMappingConstants.UserInterfaceUri.USER_ADD_URI)
	public ResponseEntity<?> createUser(@RequestBody UserModel userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.createUser(userModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** Update User Accounts

	@PutMapping(RestMappingConstants.UserInterfaceUri.UPDATE_USER_URI)
	public ResponseEntity<?> updateUser(@RequestBody UserModel userModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.updateUserData(userModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.UserInterfaceUri.UPDATE_USER_PHONE_NUMBER_URI)
	public ResponseEntity<?> updateUserPhoneNumber(@RequestBody UserUpdateModel updateModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.updatePhoneNumber(updateModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.UserInterfaceUri.UPDATE_REQUSTOR_URI)
	public ResponseEntity<?> updateCustomerRequestor(@RequestBody RequestorUpdateModel requestorUpdateModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.updateCustomerRequestor(requestorUpdateModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.UserInterfaceUri.UPDATE_USER_EMAIL_URI)
	public ResponseEntity<?> updateUserEmail(@RequestBody UserUpdateModel updateModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.updateEmail(updateModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *** Get User Accounts

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_USER_URI + "/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") int id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findUserEntityByIds(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** Get User Account By UID

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_USER_BY_UID_URI)
	public ResponseEntity<?> getUserByUId(@RequestParam("id") String id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findByUid(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** Get User FindBy PhoneNumber

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_USER_BY_PHONE_URI + "/{id}")
	public ResponseEntity<?> getUserByPrimary(@RequestParam("id") String id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findByNumber(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_ALL_USER_BY_EMAIL_URI + "/{id}")
	public ResponseEntity<?> getUserByEmail(@RequestParam("id") String email) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findByEmail(email));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_USER_BY_ENCODED_URL_URI + "/id")
	public ResponseEntity<?> getUserByEncodedURL(@RequestParam("id") String encodedUrl)
			throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findUserByUserIdURL(encodedUrl));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_ALL_USER_EMAIL_URI + "/{id}")
	public ResponseEntity<?> getAllUserEmailByCompanyId(@RequestParam("id") int id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.getAllUserEmailsByComapanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@GetMapping(RestMappingConstants.UserInterfaceUri.GET_ALL_USER_COMPANY_URI + "/{id}")
	public ResponseEntity<?> getAllUserByCompanyId(@RequestParam("id") int id) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.getAllVendorUserByCompanyId(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.UserInterfaceUri.GET_ALL_USER_LIST_URI)
	public ResponseEntity<?> getAllUserByList(@RequestBody List<Integer> ids) throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.findAllUserByIds(ids));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***Delete API TO Delete USER

	@DeleteMapping(RestMappingConstants.UserInterfaceUri.USER_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable @NotBlank int id) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.deleteUser(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@PostMapping(RestMappingConstants.UserInterfaceUri.USER_TEST_URI)
	public ResponseEntity<?> emailTesting(@RequestBody EmailTesting email) {
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.testingEmail(email));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.UserInterfaceUri.DELETE_USER_BY_EMAIL_URI + "/{email}")
	public ResponseEntity<?> deleteUserByEmail(@PathVariable @NotBlank String email) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.deleteUserByEmail(email));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PostMapping(RestMappingConstants.UserInterfaceUri.VALIDATE_EMAIL_AND_PHONE_URI)
	public ResponseEntity<?> emailAndPhoneNumberValidation(@RequestBody EmailAndPhoneValidationRequest request)
			throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.isEmailAndPhoneExist(request));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@PostMapping(RestMappingConstants.UserInterfaceUri.EMAIL_VERIFICATION_URI)
	public ResponseEntity<?> emailVerification(@RequestBody EmailRequestModel request)
			throws ResourceNotFoundException {
		BaseApiResponse baseApiResponse = null;

		baseApiResponse = ResponseBuilder.getSuccessResponse(userDetailsServiceImpl.updateEmailVerification(request));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}

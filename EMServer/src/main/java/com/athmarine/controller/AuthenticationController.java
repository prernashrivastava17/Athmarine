package com.athmarine.controller;

import com.athmarine.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.PasswordMismatchException;
import com.athmarine.request.JwtRequest;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.Base64PasswordService;
import com.athmarine.service.LoginService;

@RestController
@CrossOrigin
public class AuthenticationController {

	// @Autowired
	// private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	LoginService loginService;

	@Autowired
	Base64PasswordService passwordService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		userDetailsServiceImpl.verifyUserByEmailAndPassword(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		String emailRequest = authenticationRequest.getUsername();

		String emailToLowerCase= emailRequest.toLowerCase();
		authenticate(userDetails, emailToLowerCase, authenticationRequest.getPassword());

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(
				loginService.verifyLogin(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	private void authenticate(UserDetails userDetails, String username, String password) throws Exception {
		if (!passwordService.matches(password, userDetails.getPassword())) {
			throw new PasswordMismatchException(AppConstant.ErrorTypes.PASSWORD_MISMATCH_ERROR,
					AppConstant.ErrorCodes.PASSWORD_MISMATCH_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_CREDENTIALS_MESSAGE);
		}
	}
}

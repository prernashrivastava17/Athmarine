package com.athmarine.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.entity.User;
import com.athmarine.exception.TokenExpiredException;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.UpdatePasswordRequest;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.Base64PasswordService;
import com.athmarine.service.LoginService;
import com.athmarine.service.OTPService;
import com.athmarine.service.UserDetailsServiceImpl;

@RestController

public class LoginController {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	LoginService loginService;

	@Autowired
	OTPService OTPService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	Base64PasswordService passwordService;

	// *****GET API TO LOGIN

	@GetMapping(path = RestMappingConstants.LoginInterfaceUri.LOGIN_BASE_URI)
	public ResponseEntity<BaseApiResponse> validateOtpEmail(@Valid @NotNull @RequestParam(name = "email") String email,
			@Valid @NotNull @RequestParam(name = "password") String password) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(loginService.verifyLogin(email, password));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// API TO FORGOT PASSWORD

	@PostMapping(path = RestMappingConstants.LoginInterfaceUri.RESET_PASSWORD_URI)
	public ResponseEntity<BaseApiResponse> resetPassword(HttpServletRequest request,
			@Valid @NotNull @RequestParam("email") String userEmail) throws MessagingException {
		BaseApiResponse baseApiResponse = null;

		loginService.forgetPassword(userEmail);

		baseApiResponse = ResponseBuilder.getSuccessResponse();
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// GET API TO VALIDATE RESET PASSWORD TOKEN

	@GetMapping(RestMappingConstants.LoginInterfaceUri.RESET_PASSWORD_VERIFY_TOKEN)
	public ResponseEntity<?> showChangePasswordPage(@Valid @NotNull @RequestParam("token") String token) {
		String result = userDetailsService.validatePasswordResetToken(token);
		BaseApiResponse baseApiResponse = null;
		if (result != null) {
			throw new TokenExpiredException(AppConstant.ErrorTypes.TOKEN_EXPIRED_ERROR,
					AppConstant.ErrorCodes.TOKEN_EXPIRED_ERROR_CODE, AppConstant.ErrorMessages.TOKEN_EXPIRED_MESSAGE);
		} else {
			baseApiResponse = ResponseBuilder.getSuccessResponse();
			return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
		}
	}

	// API TO UPDATE NEW PASSWORD

	@PostMapping(RestMappingConstants.LoginInterfaceUri.UPDATE_PASSWORD)
	public ResponseEntity<?> savePassword(@Valid @RequestBody UpdatePasswordRequest request) {
		BaseApiResponse baseApiResponse = null;

		UserModel user = userDetailsService.findByEmail(request.getEmail());
		User user1 = userRepository.getUserByEmail(request.getEmail());
		/*
		 * if (user == null || user.getStatus() != 0) { throw new
		 * UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
		 * AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
		 * AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE); }
		 */

		// else {
		String password=passwordService.encode(request.getNewPassword());
		String decodedPassword=passwordService.decode(password);
		UserModel newUser = user;
		newUser.setPassword(password);
		newUser.setUid(user.getUid());
		newUser.setCompanyId(user1.getCompanyId().getId());
		userDetailsService.updateUserData(newUser);
		OTPService.sendLoginCredential(newUser, decodedPassword);
		baseApiResponse = ResponseBuilder.getSuccessResponse(newUser);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

		// }
	}
}

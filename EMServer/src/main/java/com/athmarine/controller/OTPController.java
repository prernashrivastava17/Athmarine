package com.athmarine.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.VendorCompany;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.entity.Verification_Otp;
import com.athmarine.exception.FailToSendPhoneOtpException;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.InvalidOtpException;
import com.athmarine.repository.OtpRepository;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;

@RestController
@RequestMapping(RestMappingConstants.OTPRequestUri.OTP_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OTPController {

	@Autowired
	public OTPService otpService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public UserDetailsServiceImpl userDetailsService;

	@Autowired
	SMSService smsService;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	Table_OtpService table_OtpService;

	@Autowired
	Base64PasswordService passwordService;

	@Autowired
	TwilioService twilioService;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	// **** GET API TO SEND OTP VIA EMAIL

	@GetMapping(path = RestMappingConstants.OTPRequestUri.GET_OTP_EMAIL + "/{email}")
	public ResponseEntity<BaseApiResponse> generateOTPEmail(@PathVariable String email, HttpServletRequest request)
			throws MessagingException {
		BaseApiResponse baseApiResponse = null;

//		Verification_Otp table_Otp = table_OtpService.findByEmail(email);
//		int otp = otpService.generateOTP(email);
//		if (table_Otp != null) {
//			table_Otp.setOtp(otp);
//
//		} else {
//
//			table_Otp = new Verification_Otp();
//
//			table_Otp.setEmail(email);
//			table_Otp.setOtp(otp);
//		}
//		otpRepository.save(table_Otp);
//
//		EmailTemplate template = new EmailTemplate("templates/SendOTP.html");
//		// User user1 = user.getCustomerCompany();
//
//		Map<String, String> replacementValues = new HashMap<>();
//		replacementValues.put("companyName", "Yash Vaish");
//		replacementValues.put("otp", String.valueOf(otp));
//		String message = template.getTemplate(replacementValues);
		otpService.createEmailOTP(email);
		baseApiResponse = ResponseBuilder.getSuccessResponse();
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	// *****GET API TO VALIDATE OTP VIA EMAIL

	@GetMapping(path = RestMappingConstants.OTPRequestUri.VALIDATE_OTP_EMAIL)
	public ResponseEntity<BaseApiResponse> validateOtpEmail(@RequestParam(name = "email") String email,
			@RequestParam(name = "OTP") int otpClient) {

		BaseApiResponse baseApiResponse = null;

		String username = email;

		Verification_Otp temp = table_OtpService.findByEmail(username);
		Date updatedAt = temp.getUpdatedAt();
		long updatedMins = updatedAt.getTime() / (60 * 1000);
		Date date = new Date();
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);
		long currentMins = timestamp.getTime() / (60 * 1000);
		long diff = currentMins - updatedMins;
		int serverOtp = temp.getOtp();
		 //int serverOtp=123456;
		// Validate the Otp
		if (otpClient >= 0) {

			if (serverOtp > 0) {
				if (otpClient == serverOtp) {
					if (diff <= 3 && diff >= 0) {

						// UserModel user = userDetailsService.findByEmail(username);;
						try {
							UserModel user = userDetailsService.findByEmail(username);

							if (user.getType() == null) {
								user.setEmailVerified(true);
								user.setCompanyId(user.getCompanyId());
								UserModel updateUserData = userDetailsService.updateUserData(user);

								String phone = userDetailsService.mergePhoneCodeAndNumber(user.getPhoneCode(),
										user.getPrimaryPhone());
								if(!updateUserData.isPhoneVerified()) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										otpService.createPhoneOTP(phone);
									}
								}).start();
								}
								String password;
								if (updateUserData.getPassword() == null) {
									password = null;
								} else {
									password = passwordService.decode(updateUserData.getPassword());
								}
								for (RoleModels role : user.getRoleModel()) {
									if (role.getName().equals(AppConstant.RoleValues.ROLE_ADMIN)) {

										new Thread(new Runnable() {

											@Override
											public void run() {
												otpService.sendLoginCredential(user, password);

											}
										}).start();

									} else if (role.getName().equals(AppConstant.RoleValues.ROLE_HEAD)) {

										new Thread(new Runnable() {

											@Override
											public void run() {
												//otpService.sendTermAndCondition(user.getEmail());
												otpService.sendLoginCredential(user, password);
											}
										}).start();
									}
								}
							}

							else if (user.getType().equals("Individual")) {
								user.setEmailVerified(true);
								user.setCompanyId(user.getCompanyId());
								UserModel updateUserData = userDetailsService.updateUserData(user);

								String phone = userDetailsService.mergePhoneCodeAndNumber(updateUserData.getPhoneCode(),
										user.getPrimaryPhone());
								String password = passwordService.decode(updateUserData.getPassword());
								if (updateUserData.getCompanyId() != null || updateUserData.getCompanyId() != 0) {

//								String phone = userDetailsService.mergePhoneCodeAndNumber(updateUserData.getPhoneCode(),
//										user.getPrimaryPhone());
//								String password = passwordService.decode(updateUserData.getPassword());

									if (user.isEmailVerifiedTerm() == true) {

									} else if (updateUserData.isEmailVerified() == true) {
										user.setEmailVerifiedTerm(true);
										user.setCompanyId(user.getCompanyId());
										userDetailsService.updateUserData(user);
										new Thread(new Runnable() {

											@Override
											public void run() {

												otpService.createPhoneOTP(phone);
												otpService.sendLoginCredential(user, password);
											}
										}).start();

									}
								} else {

									new Thread(new Runnable() {

										@Override
										public void run() {
											otpService.createPhoneOTP(phone);
											otpService.sendLoginCredential(user, password);
										}
									}).start();

								}

							}

							baseApiResponse = ResponseBuilder.getSuccessResponse();
						} catch (Exception e) {
							throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
									"??" + AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
									AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);
						}

						return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

					} else {
						throw new InvalidOtpException(AppConstant.ErrorTypes.INVALID_OTP_ERROR,
								AppConstant.ErrorCodes.INVALID_OTP_ERROR_CODE, AppConstant.ErrorMessages.EXPIRED_OTP);

					}
				} else {

					throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
							AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
							AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);

				}
			} else {
				throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
						AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
						AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);
			}
		} else {
			throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
					AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE, AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);
		}

	}
	// *****GET API TO VALIDATE OTP VIA SMS

	@GetMapping(path = RestMappingConstants.OTPRequestUri.VALIDATE_OTP_SMS)
	public ResponseEntity<BaseApiResponse> validateOtpSms(@RequestParam(name = "phoneCode") String phoneCode,
			@RequestParam(name = "phoneNumber") String phoneNumber, @RequestParam(name = "OTP") int otpClient) {

		BaseApiResponse baseApiResponse = null;

		String username = phoneCode.concat(phoneNumber);
		Verification_Otp temp = table_OtpService.findByPhone(username);

		Date updatedAt = temp.getUpdatedAt();
		long updatedMins = updatedAt.getTime() / (60 * 1000);
		Date date = new Date();
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);
		long currentMins = timestamp.getTime() / (60 * 1000);
		long diff = currentMins - updatedMins;

		// Validate the Otp
		if (otpClient >= 0) {
			int serverOtp = temp.getOtp();
			 //int serverOtp=123456;
			if (serverOtp > 0) {
				if (otpClient == serverOtp) {
					if (diff <= 3 && diff >= 0) {
						UserModel user = userDetailsService.findByNumber(phoneNumber);
						if (!user.isPhoneVerified()) {
							// otpService.sendLoginCredential(user, username);
							user.setPhoneVerified(true);
							UserModel updateUserData = userDetailsService.updateUserData(user);
						}
						baseApiResponse = ResponseBuilder.getSuccessResponse();
						return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

					} else {
						throw new InvalidOtpException(AppConstant.ErrorTypes.INVALID_OTP_ERROR,
								AppConstant.ErrorCodes.INVALID_OTP_ERROR_CODE, AppConstant.ErrorMessages.EXPIRED_OTP);
					}
				} else {
					throw new InvalidOtpException(AppConstant.ErrorTypes.INVALID_OTP_ERROR,
							AppConstant.ErrorCodes.INVALID_OTP_ERROR_CODE,
							AppConstant.ErrorMessages.INVALID_OTP_MESSAGE);

				}
			} else {
				throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
						AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
						AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);
			}
		} else {
			throw new InvalidInputException(AppConstant.ErrorTypes.INVALID_INPUT_ERROR,
					AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE, AppConstant.ErrorMessages.INVALID_INPUT_MESSAGE);
		}

	}

	// *** GET API TO SEND OTP VIA SMS

	@GetMapping(path = RestMappingConstants.OTPRequestUri.GET_OTP_SMS + "/{phoneNumber}")
	public ResponseEntity<BaseApiResponse> generateOTPSMS(@PathVariable @NotBlank String phoneNumber,
			HttpServletRequest request) throws MessagingException {
		BaseApiResponse baseApiResponse = null;
		Verification_Otp table_OtpModel = table_OtpService.findByPhone(phoneNumber);
		int otp = otpService.generateOTP(phoneNumber);
		if (table_OtpModel != null) {
			table_OtpModel.setOtp(otp);
		} else {

			table_OtpModel = new Verification_Otp();
			table_OtpModel.setPhone(phoneNumber);
			table_OtpModel.setOtp(otp);
		}
		otpRepository.save(table_OtpModel);

		String sendOTPPhone = String.valueOf(otp);

		try {

			// twilioService.sendMessage(phoneNumber, sendOTPPhone);
			smsService.sendVerificationSMS(phoneNumber, sendOTPPhone);

		} catch (Exception e) {
			throw new FailToSendPhoneOtpException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);
		}

		baseApiResponse = ResponseBuilder.getSuccessResponse();
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}
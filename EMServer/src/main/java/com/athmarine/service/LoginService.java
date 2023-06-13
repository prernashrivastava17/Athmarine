package com.athmarine.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.VendorCompany;
import com.athmarine.entity.Verification_Otp;
import com.athmarine.exception.FailedToSendOTPException;
import com.athmarine.exception.PasswordMismatchException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.OtpRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.AuthorizationModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.template.EmailTemplate;
import com.athmarine.util.JwtTokenUtil;

@Service
public class LoginService {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	RoleDetailsServiceImpl roleDetailsServiceImpl;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil accessToken;

	@Autowired
	public EmailService emailService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public OTPService otpService;

	@Autowired
	Table_OtpService table_OtpService;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyService companyService;

	@Autowired
	CustomerCompanyService customercompanyService;

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	Base64PasswordService passwordService;

	public AuthorizationModel verifyLogin(String email, String password) {

		UserModel user = userDetailsService.findByEmail(email);

		if (user == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_CREDENTIALS_MESSAGE);
		}
		// VendorCompany company = vendorCompanyService.findByIds(user.getCompanyId());

		// UserModel user = userDetailsService.findByUid(uid);

		String rawPassword = password;
		String phone = userDetailsService.mergePhoneCodeAndNumber(user.getPhoneCode(), user.getPrimaryPhone());

		if (passwordService.matches(rawPassword, user.getPassword()) && user.getStatus() == 0 && user.isUserAccess()) {
			if (userRepository.existsByEmail(user.getEmail())) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.sendOtpMobileAndEmail(email, phone);
					}
				}).start();

			} else {
				throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
						AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
			}

			return convertToModel(user);
		} else {
			throw new PasswordMismatchException(AppConstant.ErrorTypes.PASSWORD_MISMATCH_ERROR,
					AppConstant.ErrorCodes.PASSWORD_MISMATCH_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_CREDENTIALS_MESSAGE);

		}
	}

	public void forgetPassword(String email) throws MessagingException {
		UserModel user = userDetailsService.findByEmail(email);
		if (user == null || user.getStatus() != 0) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
		}
		String token = UUID.randomUUID().toString();
		userDetailsService.createPasswordResetTokenForUser(user, token);
		EmailTemplate template = new EmailTemplate("templates/ResetPasswordEmail.html");

		String url = AppConstant.ResetPasswordService.RESET_PASSWORD_LINK + "?userId=" + encodeUserId(email);
		Verification_Otp table_Otp = table_OtpService.findByEmail(email);
		int otp = otpService.generateOTP(email);
		String otp1 = String.valueOf(otp);

		if (table_Otp != null) {
			table_Otp.setOtp(otp);

		} else {

			table_Otp = new Verification_Otp();

			table_Otp.setEmail(email);
			table_Otp.setOtp(otp);
		}
		otpRepository.save(table_Otp);

		Map<String, String> replacementValues = new HashMap<>();
		replacementValues.put("link", url);
		replacementValues.put("otp", otp1);

		String message = template.getTemplate(replacementValues);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
				} catch (MessagingException e) {
					throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
							AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
							AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
				}
			}
		}).start();

	}

	private AuthorizationModel convertToModel(UserModel userModel) {

		RegistrationStatus registrationStatus = null;
		Boolean isRegisteredSuccessfully = null;
		String referralCode = null;
		Integer adminId = null;

		if (userModel.getCompanyId() != 0) {
			if (vendorCompanyRepository.existsById(userModel.getCompanyId())) {
				registrationStatus = companyService.findByIds(userModel.getCompanyId()).getRegistrationStatus();
				isRegisteredSuccessfully = companyService.findByIds(userModel.getCompanyId())
						.isRegisteredSuccessfully();
				referralCode = companyService.findByIds(userModel.getCompanyId()).getReferralCode();
				adminId = userDetailsService.getCompanyAdminByComapanyId(userModel.getCompanyId());
			}
			if (customerCompanyRepository.existsById(userModel.getCompanyId())) {
				registrationStatus = customercompanyService.findByIds(userModel.getCompanyId()).getRegistrationStatus();
				isRegisteredSuccessfully = customercompanyService.findByIds(userModel.getCompanyId())
						.isRegisteredSuccessfully();
				referralCode = customercompanyService.findByIds(userModel.getCompanyId()).getReferralCode();
			}
		} else {
			if (vendorCompanyRepository.existsById(userModel.getId())) {
				registrationStatus = companyService.findByIds(userModel.getId()).getRegistrationStatus();
				isRegisteredSuccessfully = companyService.findByIds(userModel.getId()).isRegisteredSuccessfully();
				if (userModel.getCompanyId() == 0 || userModel.getCompanyId() == null) {
					VendorCompany vendorCompany = companyService.findByIds(userModel.getId());
					referralCode = vendorCompany.getReferralCode();
					adminId = vendorCompany.getId();
				} else {
					referralCode = companyService.findByIds(userModel.getCompanyId()).getReferralCode();
				}
				adminId = userDetailsService.getCompanyAdminByComapanyId(userModel.getCompanyId());
			}

			if (customerCompanyRepository.existsById(userModel.getId())) {

				registrationStatus = customercompanyService.findByIds(userModel.getId()).getRegistrationStatus();
				isRegisteredSuccessfully = customercompanyService.findByIds(userModel.getId())
						.isRegisteredSuccessfully();
				referralCode = customercompanyService.findByIds(userModel.getCompanyId()).getReferralCode();
			}
		}

		return AuthorizationModel.builder().userId(userModel.getId()).name(userModel.getName())
				.email(userModel.getEmail()).primaryPhone(userModel.getPrimaryPhone())
				.individualNo(userModel.getIndividualNo())
				.companyId(userModel.getCompanyId()).userAdminId(adminId).phoneCode(userModel.getPhoneCode())
				.roleModel(userModel.getRoleModel()).token(accessToken.generateToken(userModel.getEmail()))
				.uid(userModel.getUid()).referralCode(referralCode).isRegisteredSuccessfully(isRegisteredSuccessfully)
				.registrationStatus(registrationStatus).type(userModel.getType())
				.registrationStatusKey(registrationStatus.ordinal()).build();

	}

	public String encodeUserId(String email) {
		String encodedUrl = null;
		encodedUrl = Base64.getUrlEncoder().encodeToString(email.getBytes());

		return encodedUrl;
	}

	// ****to decode the UserId****

	public String decodeUserId(String encodedUrl) {
		String decodedUrl = null;
		byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUrl);
		decodedUrl = new String(decodedBytes);

		return decodedUrl;
	}
}

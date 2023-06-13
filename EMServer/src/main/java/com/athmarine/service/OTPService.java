package com.athmarine.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Verification_Otp;
import com.athmarine.exception.FailToSendPhoneOtpException;
import com.athmarine.exception.FailedToSendOTPException;
import com.athmarine.exception.FailedToSendTermAndConditionException;
import com.athmarine.repository.OtpRepository;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.template.EmailTemplate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OTPService {

	@Autowired
	public OTPService otpService;

	@Autowired
	public EmailService emailService;

	@Autowired
	SMSService smsService;

	@Autowired
	Table_OtpService table_OtpService;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	LoginService loginService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	Base64PasswordService passwordService;

	@Autowired
	TwilioService twilioService;

	private LoadingCache<String, Integer> otpCache = null;

	public OTPService() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterAccess(AppConstant.OtpService.OTP_EXPIRY_TIME, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public int generateOTP(String key) {
		Random random = new Random();
		int otp = random.nextInt(900000) + 100000;
		otpCache.put(key, otp);

		return otp;

	}

	public int getOTP(String key) {
		try {

			return otpCache.get(key);

		} catch (Exception e) {
			e.getMessage();

			return 0;

		}
	}

	public int getOtp() {
		return 0;
	}

	public void clearOTP(String key) {
		otpCache.invalidate(key);

	}

	public boolean sendOtpMobileAndEmail(String email, String phoneNumber) {

		boolean response = false;

		try {

			Verification_Otp table_email = table_OtpService.findByEmail(email);
			int otpMail = otpService.generateOTP(email);

			if (table_email != null) {
				table_email.setOtp(otpMail);

			} else {

				table_email = new Verification_Otp();

				table_email.setEmail(email);
				table_email.setOtp(otpMail);
			}
			otpRepository.save(table_email);
			
			

			EmailTemplate template = new EmailTemplate("templates/SentOTP.html");

			String url = AppConstant.VerifyUser.VERIFY_USER_LINK + "?userId=" + loginService.encodeUserId(email);

			Map<String, String> replacementValues = new HashMap<>();

			replacementValues.put("url", url);

			replacementValues.put("otp", String.valueOf(otpMail));
			String message = template.getTemplate(replacementValues);
			Verification_Otp table_OtpModel = table_OtpService.findByPhone(phoneNumber);
			String sameOtpOfEmail = String.valueOf(otpMail);
			if (table_OtpModel != null) {
				table_OtpModel.setOtp(otpMail);
			} else {

				table_OtpModel = new Verification_Otp();
				table_OtpModel.setPhone(phoneNumber);
				table_OtpModel.setOtp(otpMail);

			}
			otpRepository.save(table_OtpModel);

			try {
				emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
				smsService.sendVerificationSMS(phoneNumber, sameOtpOfEmail);
			} catch (Exception e) {
				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
			}

			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ERROR);
		}

		return response;
	}

	public boolean createEmailOTP(String email) {
		boolean response = false;

		try {

			Verification_Otp table_email = table_OtpService.findByEmail(email);
			int otpMail = otpService.generateOTP(email);

			if (table_email != null) {
				table_email.setOtp(otpMail);

			} else {

				table_email = new Verification_Otp();

				table_email.setEmail(email);
				table_email.setOtp(otpMail);
			}
			otpRepository.save(table_email);

			EmailTemplate template = new EmailTemplate("templates/SentOTP.html");

			String url = AppConstant.VerifyUser.VERIFY_USER_LINK + "?userId=" + loginService.encodeUserId(email);

			Map<String, String> replacementValues = new HashMap<>();

			replacementValues.put("companyName", "Abhishek Raj");
			replacementValues.put("url", url);

			replacementValues.put("otp", String.valueOf(otpMail));
			String message = template.getTemplate(replacementValues);

			try {
				emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
			} catch (Exception e) {

//				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
//						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
//						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
			}
			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ERROR);
		}
		return response;
	}

	public boolean createPhoneOTP(String phoneNumber) {
		boolean response = false;

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

			//smsService.sendVerificationSMS(phoneNumber, sendOTPPhone);
			twilioService.sendMessage(phoneNumber, sendOTPPhone);
		} catch (Exception e) {
			throw new FailToSendPhoneOtpException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);
		}

		response = true;

		return response;
	}

	public boolean createEmailVerificationLink(String email) {
		boolean response = false;

		try {

			EmailTemplate template = new EmailTemplate("templates/Verify Account.html");
			String url = AppConstant.VerifyUser.VERIFY_USER_LINK + "?userId=" + loginService.encodeUserId(email);

			Map<String, String> replacementValues = new HashMap<>();
			
			replacementValues.put("url", url);
			String message = template.getTemplate(replacementValues);

			try {
				emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
			} catch (Exception e) {
				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
			}
			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ERROR);
		}
		return response;
	}

	public boolean createEmailAndPhoneOTP2(String email, String phoneNumber) {
		boolean response = false;

		try {

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

			Verification_Otp table_email = table_OtpService.findByEmail(email);
			int otpMail = otpService.generateOTP(email);

			if (table_email != null) {
				table_email.setOtp(otpMail);

			} else {

				table_email = new Verification_Otp();

				table_email.setEmail(email);
				table_email.setOtp(otpMail);
			}
			otpRepository.save(table_email);

			EmailTemplate template = new EmailTemplate("templates/Verify Account.html");
			String url = AppConstant.VerifyUser.VERIFY_USER_LINK + "?userId=" + loginService.encodeUserId(email);

			Map<String, String> replacementValues = new HashMap<>();

			replacementValues.put("companyName", "Yash Vaish");

			replacementValues.put("otp", String.valueOf(otp));
			replacementValues.put("url", url);
			String message = template.getTemplate(replacementValues);

			try {
				emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
			} catch (Exception e) {
				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
			}
			try {

				//smsService.sendVerificationSMS(phoneNumber, sendOTPPhone);
				twilioService.sendMessage(phoneNumber, sendOTPPhone);

			} catch (Exception e) {
				throw new FailToSendPhoneOtpException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);
			}

			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ERROR);
		}
		return response;
	}

	public boolean sendTermAndCondition(String email) {

		EmailTemplate template = new EmailTemplate("templates/TermsAndConditions.html");

		String url = AppConstant.TermsAndConditions.TERMS_AND_CONDITIONS_LINK + "?userId="
				+ loginService.encodeUserId(email);

		Map<String, String> replacementValues = new HashMap<>();

		replacementValues.put("url", url);

		String message = template.getTemplate(replacementValues);

		try {
			emailService.sendEmail(email, AppConstant.TermsAndConditions.TERMS_AND_CONDITIONS_SUBJECT_LINE, message);
		} catch (Exception e) {
			throw new FailedToSendTermAndConditionException(AppConstant.ErrorTypes.FAILED_TO_SEND_TERM_AND_CONDITION,
					AppConstant.ErrorCodes.FAILED_TO_SEND_TERM_AND_CONDITION_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_TERM_AND_CONDITION_ERROR);
		}

		return true;
	}

	@SuppressWarnings("unused")
	public boolean sendLoginCredentials(String email, String password, String phoneNumber) {
		boolean response = false;

		try {

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

			EmailTemplate template = new EmailTemplate("templates/SendLoginCredentials.html");

			Map<String, String> replacementValues = new HashMap<>();
			replacementValues.put("username", email);
			String string = passwordService.encode(password);
			replacementValues.put("password", passwordService.decode(string));
			String message = template.getTemplate(replacementValues);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
					} catch (Exception e) {
						throw new FailedToSendOTPException(
								AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
								AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
								AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
					}

				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						
						//smsService.sendVerificationSMS(phoneNumber, sendOTPPhone);
						twilioService.sendMessage(phoneNumber, sendOTPPhone);


					} catch (Exception e) {
						throw new FailToSendPhoneOtpException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
								AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
								AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);
					}

				}
			}).start();
			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
		}
		return response;
	}

	public boolean sendLoginCredential(UserModel model, String password) {
		boolean response = false;

		try {

			EmailTemplate template = new EmailTemplate("templates/SendLoginCredentials.html");

			Map<String, String> replacementValues = new HashMap<>();
			replacementValues.put("username", model.getEmail());

			String string = passwordService.encode(password);
			String passwordString = passwordService.decode(string);
			replacementValues.put("password", passwordString);
			String message = template.getTemplate(replacementValues);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						emailService.sendEmail(model.getEmail(), AppConstant.OtpService.OTP_SUBJECT_LINE, message);
					} catch (Exception e) {
						throw new FailedToSendOTPException(
								AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
								AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
								AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
					}

				}
			}).start();
			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
		}
		return response;
	}
	
	public boolean sendInvoiceToFinance(String email,String amount) {
		boolean response = false;

		try {

			EmailTemplate template = new EmailTemplate("templates/Invoice.html");

			Map<String, String> replacementValues = new HashMap<>();
			replacementValues.put("Amount", amount);
			String message = template.getTemplate(replacementValues);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						emailService.sendEmail(email, AppConstant.OtpService.OTP_SUBJECT_LINE, message);
					} catch (Exception e) {
						throw new FailedToSendOTPException(
								AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
								AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
								AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
					}

				}
			}).start();
			response = true;

		} catch (Exception e) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR);
		}
		return response;
	}
}


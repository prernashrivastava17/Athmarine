package com.athmarine.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.PasswordResetToken;
import com.athmarine.entity.Role;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.entity.Verification_Otp;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.FailedToSendOTPException;
import com.athmarine.exception.ManufacturerNotFoundException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.PhoneNumberNotFoundException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.TokenExpiredException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.OtpRepository;
import com.athmarine.repository.PasswordResetTokenRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.CompanyModel;
import com.athmarine.request.EmailAndPhoneValidationRequest;
import com.athmarine.request.EmailRequestModel;
import com.athmarine.request.EmailTesting;
import com.athmarine.request.FinanceEmailAndPhoneNumberResponse;
import com.athmarine.request.RequestorUpdateModel;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.request.UserUpdateModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.UserEmailPhoneResponseModel;
import com.athmarine.template.EmailTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@PersistenceContext
	EntityManager entityManger;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;

	@Autowired
	RoleDetailsServiceImpl roleDetailsServiceImpl;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	CustomerCompanyService customerCompanyService;

	@Autowired
	AvailableOnService availableOnService;

	@Autowired
	OTPService otpService;

	@Autowired
	SMSService smsService;

	@Autowired
	public EmailService emailService;

	@Autowired
	Table_OtpService table_OtpService;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	CustomerCompanyService companyService;

	@Autowired
	LoginService loginService;

	@Autowired
	Base64PasswordService passwordService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	MasterCountryService countryService;

	@Autowired
	EngineerChargesService engineerChargesService;

	@Autowired
	TwilioService twilioService;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.getUserByEmail(email);

		// User user = userRepository.getUserByUid(uid);
		if (user == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_NOT_EXIST_MESSAGE);
		}
		return new TransasUserDetails(user);

	}

	public String mergePhoneCodeAndNumber(final String phoneCode, String primaryPhone) {
		return phoneCode.concat(primaryPhone);

	}

	public UserModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		} else {
			return convertToModel(userRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
							AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));
		}

	}

	public UserModel getUserHeadByCompanyId(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		} else {
			return convertToModel(userRepository.getUserHeadByCompanyId(id));
		}

	}

	public CompanyModel findByCompanyId(int id) {
		return convertToModels(userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));

	}

	public User findCompanyById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		} else {

			return userRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
							AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE));

		}
	}

	public User findByIds(Integer id) {

		UserModel user = findById(id);
		if (user.getCompanyId() != null) {

			if (id != null) {
				return userRepository.findById(id)
						.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
								AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
								AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE));

			} else {
				throw new ManufacturerNotFoundException(AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

			}

		} else {
			throw new ManufacturerNotFoundException(AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE, AppConstant.ErrorMessages.COMPANY_ID_EMPTY_MESSAGE);
		}

	}

	public EmailTesting testingEmail(EmailTesting email) {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
				otpService.createEmailOTP(email.getEmail());

//			}
//		}).start();

		return email;

	}

	public UserModel createUser(UserModel userModel) {

		User company = null;

		if (userModel.getCompanyId() != null) {

			company = convertToEntity(findById(userModel.getCompanyId()), company);
		}

		if (userRepository.existsByEmail(userModel.getEmail())) {

			throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

		}

		if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {

			throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
					AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

		}
		userModel.setImageUrl("UploadDocument/Avtar20665B1626353485030.png");
		return convertToModel(userRepository.save(convertToEntity(userModel, company)));
	}

	public UserModel createCompany(UserModel userModel) {

		if (userRepository.existsByEmail(userModel.getEmail())) {
			UserModel findEmail = findByEmail(userModel.getEmail());
			if (findEmail.getPrimaryPhone().equals(userModel.getPrimaryPhone())) {
				userModel.setId(findEmail.getId());
				userModel.setCompanyId(findEmail.getCompanyId());
				UserModel userModelCompany = updateUserData(userModel);
				return userModelCompany;
			} else {
				throw new ResourceNotFoundException(AppConstant.ErrorTypes.PRIMARY_PHONE_EXIST_ERROR,
						AppConstant.ErrorCodes.PRIMARY_PHONE_ERROR_CODE,
						AppConstant.ErrorMessages.CONTACT_NO_ALREADY_EXIST_MESSAGE);
			}

		}
		userModel.setImageUrl("UploadDocument/Avtar20665B1626353485030.png");
		if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.PRIMARY_PHONE_EXIST_ERROR,
					AppConstant.ErrorCodes.PRIMARY_PHONE_ERROR_CODE, AppConstant.ErrorMessages.CONTACT_NO_ALREADY_EXIST_MESSAGE);

		}
		return convertToModel(userRepository.save(convertToEntity(userModel)));
	}

	public UserModel updateCompany(UserModel userModel) {
//		VendorCompany company =vendorCompanyService.findByIds(userModel.getId());
		User user = findByIds(userModel.getId());
		user.setAvailableOn(userModel.getAvailableOn());
		user.setCompanyId(user);
		user.setPassword(passwordService.encode(userModel.getPassword()));
		return convertToModel(userRepository.save(user));
	}
	
	public EmailRequestModel updateEmailVerification(EmailRequestModel userModel) {
		User user = findByEmails(userModel.getEmail());
		user.setEmailVerified(true);
		return convertToModelEmailVerification(userRepository.save(user));
	}

	@SuppressWarnings("unused")
	public UserModel updateUserData(UserModel userModel) {

		User company = null;

		User approver = null;
		UserModel findId = findById(userModel.getId());
		if (findId.getApproverId() != 0) {
			approver = findByIds(userModel.getApproverId());
		}

		if (findId.getCompanyId() != 0) {

			CompanyModel companyModel = findByCompanyId(userModel.getCompanyId());
			company = convertToEntityUpdate(findById(userModel.getCompanyId()), null);
			company.setUid(companyModel.getUid());
		}

		if (userModel.getId() != null) {
			if (findId == null) {
				throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
						AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

			} else {
				User user = convertToEntityApproverUpdate(userModel, company, approver);
				user.setEmailVerified(userModel.isEmailVerified());
				user.setPhoneVerified(user.isPhoneVerified());
				user.setUid(userModel.getUid());
				user.setPassword(userModel.getPassword());
				user.setUserAccess(userModel.isUserAccess());
				return convertToModel(userRepository.save(user));
			}

		} else {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		}
	}

	public UserModel updateUserBidderData(UserModel userModel) {

		if (userModel.getId() == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);

		} else {
			User user = convertToEntitySame(userModel);
			userRepository.save(user);
			return userModel;

		}

	}

	@SuppressWarnings("unused")
	public void updateUserDataCustomer(List<User> userModel) {

		User company = null;

		User approver = null;

		UserModel findId = findById(userModel.get(0).getId());

		if (userModel.get(0).getCompanyId() != null) {
			CompanyModel companyModel = findByCompanyId(userModel.get(0).getCompanyId().getId());
			company = convertToEntityUpdate(findById(userModel.get(0).getCompanyId().getId()), null);
			company.setUid(companyModel.getUid());
		}

		if (userModel.get(0).getApproverId() != null) {

			approver = userModel.get(0).getApproverId();
		}
		if (userModel.get(0).getId() != null) {
			if (findId == null) {
				throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);

			} else {
				userRepository.saveAll(userModel);
			}

		} else {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		}

	}

	public UserModel updatePhoneNumber(UserUpdateModel updateModel) {

		UserModel userModel = findUserEntityByIds(updateModel.getUserId());

		if (userRepository.existsByPrimaryPhone(updateModel.getNewPrimaryPhone())
				&& !userModel.getPrimaryPhone().equals(updateModel.getNewPrimaryPhone())) {
			throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
					AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE, AppConstant.ErrorMessages.CONTACT_NO_EXIST_OR_INVALID);
		} else {

			UserModel model = findById(updateModel.getUserId());
			model.setPhoneCode(updateModel.getNewPhoneCode());
			model.setPrimaryPhone(updateModel.getNewPrimaryPhone());
			model = updateUserData(model);

			String phone = mergePhoneCodeAndNumber(updateModel.getNewPhoneCode(), updateModel.getNewPrimaryPhone());
			Verification_Otp table_OtpModel = table_OtpService.findByPhone(phone);
			int otp = otpService.generateOTP(phone);
			if (table_OtpModel != null) {
				table_OtpModel.setOtp(otp);
			} else {

				table_OtpModel = new Verification_Otp();
				table_OtpModel.setPhone(phone);
				table_OtpModel.setOtp(otp);
			}
			otpRepository.save(table_OtpModel);
			String sendOTP = String.valueOf(otp);
			try {
				twilioService.sendMessage(phone, sendOTP);
			} catch (Exception e) {
				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);

			}
			// smsService.sendVerificationSMS(phone, sendOTP);
			return model;
		}
	}

	public UserModel updateEmail(UserUpdateModel updateModel) {

		UserModel userModel = findUserEntityByIds(updateModel.getUserId());

		if (userRepository.existsByEmail(updateModel.getEmail())
				&& !userModel.getEmail().equals(updateModel.getEmail())) {
			throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
					updateModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
		} else {

			UserModel model = findById(updateModel.getUserId());
			model.setEmail(updateModel.getEmail());
			model = updateUserData(model);

			Verification_Otp table_Otp = table_OtpService.findByEmail(updateModel.getEmail());
			int otp = otpService.generateOTP(updateModel.getEmail());

			if (table_Otp != null) {
				table_Otp.setOtp(otp);

			} else {

				table_Otp = new Verification_Otp();

				table_Otp.setEmail(updateModel.getEmail());
				table_Otp.setOtp(otp);
			}
			otpRepository.save(table_Otp);

			EmailTemplate template = new EmailTemplate("templates/SentOTP.html");

			String url = AppConstant.VerifyUser.VERIFY_USER_LINK + "?userId="
					+ loginService.encodeUserId(updateModel.getEmail());

			Map<String, String> replacementValues = new HashMap<>();
			replacementValues.put("url", url);
			replacementValues.put("otp", String.valueOf(otp));
			String message = template.getTemplate(replacementValues);
			try {
				emailService.sendEmail(updateModel.getEmail(), AppConstant.OtpService.OTP_SUBJECT_LINE, message);
			} catch (MessagingException e) {
				throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR,
						AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE,
						AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_MAIL_ERROR);
			}

			return model;
		}
	}

	public UserModel deleteUser(Integer id) {
		User user = findByIds(id);
		user.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
		return convertToModel(userRepository.save(user));
	}

	public String deleteUserByEmail(@NotBlank String email) {

		User user = userRepository.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);

		} else {
			user.setEmail("deleted");
			user.setPrimaryPhone("deleted");
			user.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
			convertToModel(userRepository.save(user));
			return "User successfully deleted";
		}
	}

	public UserModel findUserEntityByIds(@NotNull Integer id) {

		return convertToModel(userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));

	}

	public UserEmailPhoneResponseModel findUserByUserIdURL(String encodedUrl) {
		String email = loginService.decodeUserId(encodedUrl);
		return convertToModelUserEmailPhoneModel(findByEmail(email));
	}

	public List<UserModel> findAllUserByIds(List<Integer> ids) {

		List<UserModel> response = new ArrayList<UserModel>();
		for (Integer i : ids) {
			response.add(findById(i));
		}
		return response;
	}

	public String updateCustomerRequestor(RequestorUpdateModel requestorUpdateModel) {

		UserModel model = findById(requestorUpdateModel.getId());
		model.setId(requestorUpdateModel.getId());
		model.setAddress(requestorUpdateModel.getAddress());
		model.setName(requestorUpdateModel.getName());
		model.setEmail(requestorUpdateModel.getEmail());
		model.setDob(requestorUpdateModel.getDob());
		model.setPhoneCode(requestorUpdateModel.getPhoneCode());
		model.setPrimaryPhone(requestorUpdateModel.getPrimaryPhone());
		model.setPassword(passwordEncoder.encode(requestorUpdateModel.getPassword()));
		model.setImageUrl(requestorUpdateModel.getImageUrl());
		model.setUid(model.getUid());
		model = updateUserData(model);

		return "User Profile Sucessfully Uploaded";
	}

	public RequestorUpdateModel getRequestorProfileByRequestorId(Integer id) {
		UserModel userModel = findById(id);
		return convaertToRequestorModel(userModel);

	}

	public User findUserEntityById(int id) {

		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
	}
	
	public User findByEmails(String email) {
		User user = userRepository.getUserByEmail(email);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
		}
	}

	public UserModel findByEmail(String email) {
		User user = userRepository.getUserByEmail(email);
		if (user != null) {
			return convertToModel(user);
		} else {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
		}
	}

	public UserModel findByNumber(String primaryPhone) {

		User user = userRepository.getUserByPrimaryPhone(primaryPhone);

		if (user.getId() != null) {
			return convertToModel(user);
		} else {
			throw new UserNotFoundException(AppConstant.ErrorTypes.PHONE_NUMBER_DOES_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.PHONE_NUMBER_DOES_NOT_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.PHONE_NUMBER_DOES_NOT_EXIST_ERROR);
		}
	}

	// get all user emails of company by company id
	public List<String> getAllUserEmailsByComapanyId(@NotNull int id) {
		List<UserModel> userList = getAllVendorUserByCompanyId(id);

		return userList.stream().map(user -> user.getEmail()).collect(Collectors.toList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserModel> getAllVendorUserByCompanyId(Integer id) {

		try {
			VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
			return vendorCompany.getUser().getListUser().stream().map(user -> convertToUserModel(user))
					.collect(Collectors.toList());
		} catch (Exception e) {
			return new ArrayList();
		}
	}

	public List<UserModel> getAllVendorUsersByCompanyId(Integer id) {

		try {
			String finance = "Finance";
			VendorCompany vendorCompany = vendorCompanyService.findByIds(id);

			return vendorCompany.getUser().getListUser().stream()
					.filter(request -> finance.equals(AppConstant.RoleValues.ROLE_FINANCE))
					.map(user -> convertToModel(user)).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
	}

	public List<UserModel> getAllVendorUserByCompanyIdCompy(Integer id) {

		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
		return vendorCompany.getUser().getListUser().stream().map(user -> convertToModel(user))
				.collect(Collectors.toList());

	}

	public List<User> getAllUserByVendorCompanyId(Integer id) {

		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
		return vendorCompany.getUser().getListUser();

	}

	public Integer getCompanyAdminByComapanyId(Integer id) {

		List<UserModel> allUser = getAllVendorUserByCompanyId(id);
		Integer cid;
		try {
			cid = allUser.stream().findFirst().get().getId();
		} catch (Exception e) {
			cid = 0;
		}
		return cid;

	}

	public List<UserModel> getAllCustomerUserByCompanyId(@NotNull int id) {

		CustomerCompany customerCompany = customerCompanyService.findByIds(id);

		return customerCompany.getUserCustomer().getListUser().stream().map(user -> convertToModel(user))
				.collect(Collectors.toList());
	}

	public List<User> getAllUserByCustomerCompanyId(@NotNull int id) {

		CustomerCompany customerCompany = customerCompanyService.findByIds(id);

		return customerCompany.getUserCustomer().getListUser().stream().collect(Collectors.toList());
	}

	public List<UserModel> getAllCustomerByApproverId(@NotBlank Integer id) {
		UserModel user = findById(id);
		if (companyService.checkAlreadyExists(user.getCompanyId())) {

			List<UserModel> userList = getAllCustomerUserByCompanyId(user.getCompanyId());

			List<UserModel> userResponse = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				if (userList.get(i).getApproverId().equals(id)) {

					userResponse.add(userList.get(i));
				}
			}
			return userResponse;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> findAllByApproverId(Integer id) {

		User customerCompany = findByIds(id);

		List<User> customerCompany12 = userRepository.findAllByApproverId(customerCompany);
		return customerCompany12.stream().map(user -> convertToModel(user)).collect(Collectors.toList());
	}

	public UserModel findByUid(String uid) {

		User user = userRepository.getUserByUid(uid);

		if (user != null) {
			return convertToModel(user);
		} else {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
		}
	}

	public Boolean isEmailAndPhoneExist(EmailAndPhoneValidationRequest request) {

		Boolean existByEmail = false;
		Boolean existByPhone = false;
		if (request.getEmail() == null) {
			existByEmail = false;
		} else {
			existByEmail = userRepository.existsByEmail(request.getEmail());
		}

		if (request.getPhoneNumber() == null) {
			existByPhone = false;
		} else {
			existByPhone = userRepository.existsByPrimaryPhone(request.getPhoneNumber());
		}

		if (existByEmail) {
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.USER_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_ALREADY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_AND_PHONE_ALREADY_EXIST);
		}
		if (existByPhone) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.PHONE_ALREADY_EXIST);
		}
		return true;
	}

	public void createPasswordResetTokenForUser(UserModel user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, convertToEntity(user, null));
		List<PasswordResetToken> checkToken = passwordTokenRepository.findAllByuserId(user.getId());
		if (checkToken != null) {
			for (PasswordResetToken eachRow : checkToken) {
				passwordTokenRepository.deleteById(eachRow.getId());

			}

		}
		passwordTokenRepository.save(myToken);
	}

	public String validatePasswordResetToken(String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		if (passToken == null) {
			throw new TokenExpiredException(AppConstant.ErrorTypes.TOKEN_EXPIRED_ERROR,
					AppConstant.ErrorCodes.TOKEN_EXPIRED_ERROR_CODE, AppConstant.ErrorMessages.TOKEN_EXPIRED_MESSAGE);

		}

		return !isTokenFound(passToken) ? "invalidToken" : isTokenExpired(passToken) ? "expired" : null;
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
		final Calendar cal = Calendar.getInstance();
		return passToken.getExpiryDate().before(cal.getTime());
	}

	public List<UserModel> getAllUserByCompanyId(Integer id) {
		User users = findByIds(id);
		List<User> userList = userRepository.findAllByListUser(users);
		return userList.stream().map(userCompany -> convertToModel(userCompany)).collect(Collectors.toList());
	}

	public Integer getAllEngineerByCompanyId(Integer id) {

		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);

		Integer count = userRepository.getAllEngineerByCompanyId(vendorCompany.getId());

		return count;

	}


	public Integer getAllBidderByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		return userRepository.getAllBidderByCompanyId(company.getId());

	}

	public Integer getAllAdminByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		return userRepository.getAllAdminByCompanyId(company.getId());

	}
	
	public Integer getAllCustomerAdminByCompanyId(Integer id) {
		CustomerCompany company = customerCompanyService.findByIds(id);
		return userRepository.getAllAdminByCompanyId(company.getId());

	}

	public Integer getAllVendorHeadByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		return userRepository.getAllVendorHeadByCompanyId(company.getId());

	}

	public Integer getAllVendorApproverByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		return userRepository.getAllVendorApproverByCompanyId(company.getId());

	}

	public Integer getAllFinancerByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		return userRepository.getAllFinancerByCompanyId(company.getId());
	}

	public List<UserModel> getAllFinanceByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		List<User> listOfFinacer = userRepository.getAllFinanceByCompanyId(company.getId());
		return listOfFinacer.stream().map(finaner -> convertToModel(finaner)).collect(Collectors.toList());
	}

	public List<UserModel> getAllEngineersByCompanyId(Integer id) {
		VendorCompany company = vendorCompanyService.findByIds(id);
		List<User> engineerList = userRepository.getAllEngineersByCompanyId(company.getId());
		return engineerList.stream().sorted(Comparator.comparing(User::getName)).map(e -> convertToModel(e))
				.collect(Collectors.toList());

	}
	
	public Integer getAllRequestorByCompanyId(Integer id) {
		CustomerCompany company = customerCompanyService.findByIds(id);
		return userRepository.getAllRequestorByCompanyId(company.getId());

	}

	public String generateUniqueIdForCustomer(Integer id, int count) {

		UserModel getUserData = findById(id);
		StringBuffer uniqueCode = new StringBuffer("C");
		CustomerCompany customerCompany = customerCompanyService.findByIds(getUserData.getCompanyId());
		uniqueCode.append(customerCompany.getCountryId().getShortName());
		String role = getUserData.getRoleModel().get(0).getName();

		if (role.equalsIgnoreCase("Admin")) {
			uniqueCode.append("D");

		} else if (role.equalsIgnoreCase("Approver")) {
			uniqueCode.append("A");

		} else if (role.equalsIgnoreCase("Requester")) {
			uniqueCode.append("R");

		} else if (role.equalsIgnoreCase("Head")) {
			uniqueCode.append("M");

		} else if (role.equalsIgnoreCase("Finance")) {
			uniqueCode.append("F");

		}
		uniqueCode.append(LocalDateTime.now().getYear() % 2000);
		uniqueCode.append(String.format("%04d", count));

		return uniqueCode.toString();
	}

	public String generateUniqueIdVendor(Integer id, Integer count) {

		UserModel getUserData = findById(id);
		StringBuffer uniqueCode = new StringBuffer("");
		VendorCompany vendorCompany = vendorCompanyService.findByIds(getUserData.getCompanyId());
		uniqueCode.append(vendorCompany.getReferralCode());
		String role = getUserData.getRoleModel().get(0).getName();

		if (role.equalsIgnoreCase("Admin")) {
			uniqueCode.append("D");

		} else if (role.equalsIgnoreCase("Engineer")) {
			uniqueCode.append("E");

		} else if (role.equalsIgnoreCase("Bidder")) {
			uniqueCode.append("B");

		} else if (role.equalsIgnoreCase("Approver")) {
			uniqueCode.append("A");

		} else if (role.equalsIgnoreCase("Head")) {
			uniqueCode.append("M");

		} else if (role.equalsIgnoreCase("Finance")) {
			uniqueCode.append("F");

		}
		User latestUser = userRepository.findFirstByOrderByIdDesc();
//		LocalDateTime time = latestUser.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime time = LocalDateTime.now();

		uniqueCode.append(LocalDateTime.now().getYear() % 2000);
		if (time.getYear() == LocalDateTime.now().getYear()) {

		} else {
			uniqueCode.append(String.format("%02d"));
		}
		// uniqueCode.append(LocalDateTime.now().getYear() % 2000);
		uniqueCode.append(String.format("%02d", count));
		return uniqueCode.toString();
	}

	public User convertToEntity(UserModel model, User companyId) {

		String uids = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 6).toUpperCase();

		String password = null;

		if (model.getPassword() == null) {
			password = null;

		} else {
			password = passwordService.encode(model.getPassword());
		}

		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		for (RoleModels tryone : list) {
			roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
		}

		return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
				.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
				.imageUrl(model.getImageUrl()).designation(model.getDesignation()).uid(uids).address(model.getAddress())
				.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
				.sameASUser(model.getSameASUser()).isOnWhatsapp(model.isOnWhatsapp())
				.individualNo(model.getIndividualNo()).isEmailVerifiedTerm(model.isEmailVerifiedTerm())
				.biddingLimit(model.getBiddingLimit()).password(password).status(model.getStatus()).role(roleList)
				.phoneCode(model.getPhoneCode()).currency(model.getCurrency()).availableOn(model.getAvailableOn())
				.companyId(companyId).userAccess(true).build();
	}

	public UserModel updateSameUser(UserModel userModel) {

		UserModel findId = findByEmail(userModel.getEmail());
		if (findId.getPrimaryPhone().equals(userModel.getPrimaryPhone())) {
			if (userModel.getCurrency() == null) {
				findId.setCurrency(findId.getCurrency());
			} else {
				findId.setCurrency(userModel.getCurrency());
			}
			if (userModel.getBiddingLimit() == null) {
				findId.setBiddingLimit(findId.getBiddingLimit());
			} else {
				findId.setBiddingLimit(userModel.getBiddingLimit());
			}
			if (userModel.getDesignation() == null) {
				findId.setDesignation(findId.getDesignation());
			} else {
				findId.setDesignation(userModel.getDesignation());
			}
			if (findId.getPassword() == null) {
				if (userModel.getPassword() == null) {
					findId.setPassword(userModel.getPassword());
				}
				else {
					findId.setPassword(passwordService.encode(userModel.getPassword()));
				}
			} else {
				findId.setPassword(findId.getPassword());
			}
			if(userModel.isOnWhatsapp()==false) {
				findId.setOnWhatsapp(findId.isOnWhatsapp());
			}
			else {
				findId.setOnWhatsapp(userModel.isOnWhatsapp());
			}
			if(userModel.getAvailableOn()==null) {
				findId.setAvailableOn(findId.getAvailableOn());
			}
			else {
				findId.setAvailableOn(userModel.getAvailableOn());
			}
			if (userRepository.existsByEmail(userModel.getEmail())) {
				findId.setUid(findId.getUid());
				findId.setEmailVerified(true);
				findId.setSameASUser(userModel.getSameASUser());
				findId.getRoleModel().addAll((userModel.getRoleModel()));
				return updateUserBidderData(findId);
			} else {
				throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
			}

		} else {
			throw new PhoneNumberNotFoundException(AppConstant.ErrorTypes.PHONE_NUMBER_DOES_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.PHONE_NUMBER_DOES_NOT_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.PHONE_NUMBER_DOES_NOT_EXIST_ERROR);

		}

	}

	public User convertToEntityUpdate(UserModel model, User companyId) {

		String uids = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 6).toUpperCase();
		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		for (RoleModels tryone : list) {
			roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
		}
		if (model.getPassword() != null) {
			return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
					.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
					.imageUrl(model.getImageUrl()).designation(model.getDesignation()).uid(model.getUid())
					.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
					.isEmailVerifiedTerm(model.isEmailVerifiedTerm()).address(model.getAddress())
					.biddingLimit(model.getBiddingLimit()).password(model.getPassword()).status(model.getStatus())
					.role(roleList).companyId(companyId).isOnWhatsapp(model.isOnWhatsapp())
					.individualNo(model.getIndividualNo()).sameASUser(model.getSameASUser())
					.phoneCode(model.getPhoneCode()).type(model.getType()).currency(model.getCurrency())
					.availableOn(model.getAvailableOn()).build();

		} else {

			return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
					.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
					.designation(model.getDesignation()).uid(model.getUid()).isPhoneVerified(model.isPhoneVerified())
					.isEmailVerified(model.isEmailVerified()).status(model.getStatus()).address(model.getAddress())
					.designation(model.getDesignation()).uid(uids).imageUrl(model.getImageUrl())
					.sameASUser(model.getSameASUser()).isPhoneVerified(model.isPhoneVerified())
					.individualNo(model.getIndividualNo()).isEmailVerified(model.isEmailVerified())
					.isEmailVerifiedTerm(model.isEmailVerifiedTerm()).status(model.getStatus()).role(roleList)
					.isOnWhatsapp(model.isOnWhatsapp()).phoneCode(model.getPhoneCode()).currency(model.getCurrency())
					.type(model.getType()).availableOn(model.getAvailableOn()).companyId(companyId).build();

		}

	}

	public UserModel convertToUserModel(User entity) {
		String password = null;
		List<Role> listofList = entity.getRole();

		List<RoleModels> roleModel = new ArrayList<>();

		for (Role role : listofList) {

			roleModel.add(roleDetailsServiceImpl.findByRoleId(role.getId()));

		}

		if (entity.getPassword() == null) {
			password = null;
		} else {
			password = passwordService.decode(entity.getPassword());
		}

		return UserModel.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.type(entity.getType()).primaryPhone(entity.getPrimaryPhone()).dob(entity.getDob())
				.secondaryPhone(entity.getSecondaryPhone()).designation(entity.getDesignation())
				.phoneVerified(entity.isPhoneVerified()).password(password).emailVerified(entity.isEmailVerified())
				.status(entity.getStatus()).sameASUser(entity.getSameASUser()).individualNo(entity.getIndividualNo())
				.emailVerifiedTerm(entity.isEmailVerifiedTerm()).biddingLimit(entity.getBiddingLimit())
				.uid(entity.getUid()).currency(entity.getCurrency()).roleModel(roleModel).address(entity.getAddress())
				.phoneCode(entity.getPhoneCode()).onWhatsapp(entity.isOnWhatsapp()).imageUrl(entity.getImageUrl())
				.companyId((entity.getCompanyId() == null) ? 0 : entity.getCompanyId().getId())
				.approverId((entity.getApproverId() == null) ? 0 : entity.getApproverId().getId())
				.availableOn(entity.getAvailableOn()).userAccess(entity.isUserAccess()).build();
	}

	public UserModel convertToModel(User entity) {

		List<Role> listofList = entity.getRole();

		List<RoleModels> roleModel = new ArrayList<>();

		for (Role role : listofList) {

			roleModel.add(roleDetailsServiceImpl.findByRoleId(role.getId()));

		}

		return UserModel.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.type(entity.getType()).primaryPhone(entity.getPrimaryPhone()).dob(entity.getDob())
				.secondaryPhone(entity.getSecondaryPhone()).designation(entity.getDesignation())
				.phoneVerified(entity.isPhoneVerified()).password(entity.getPassword())
				.individualNo(entity.getIndividualNo()).emailVerified(entity.isEmailVerified())
				.status(entity.getStatus()).sameASUser(entity.getSameASUser())
				.emailVerifiedTerm(entity.isEmailVerifiedTerm()).biddingLimit(entity.getBiddingLimit())
				.uid(entity.getUid()).currency(entity.getCurrency()).roleModel(roleModel).address(entity.getAddress())
				.phoneCode(entity.getPhoneCode()).onWhatsapp(entity.isOnWhatsapp()).imageUrl(entity.getImageUrl())
				.companyId((entity.getCompanyId() == null) ? entity.getId() : entity.getCompanyId().getId())
				.approverId((entity.getApproverId() == null) ? 0 : entity.getApproverId().getId())
				.availableOn(entity.getAvailableOn()).userAccess(entity.isUserAccess()).build();
	}

	public User convertToEntity(CompanyModel model, User companyId) {

		String uids = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 6).toUpperCase();

		return User.builder().id(model.getId()).name(model.getEmail()).email(model.getEmail()).type(model.getType())
				.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone())
				.address(model.getAddress()).isEmailVerified(model.isEmailVerified()).uid(uids)
				.isPhoneVerified(model.isPhoneVerified()).isOnWhatsapp(model.isOnWhatsapp())
				.isEmailVerifiedTerm(model.isEmailVerifiedTerm()).phoneCode(model.getPhoneCode())
				// .role(model.getRole())
				.individualNo(model.getIndividualNo()).currency(model.getCurrency()).companyId(companyId)
				.status(model.getStatus()).build();

	}

	public CompanyModel convertToModels(User entity) {

		return CompanyModel.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.type(entity.getType()).primaryPhone(entity.getPrimaryPhone()).address(entity.getAddress())
				.secondaryPhone(entity.getSecondaryPhone()).designation(entity.getDesignation())
				.isPhoneVerified(entity.isPhoneVerified()).isEmailVerified(entity.isEmailVerified())
				.uid(entity.getUid()).status(entity.getStatus()).individualNo(entity.getIndividualNo())
				// .role(entity.getRole())
				.emailVerifiedTerm(entity.isEmailVerifiedTerm()).availableOn(entity.getAvailableOn())
				.phoneCode(entity.getPhoneCode()).currency(entity.getCurrency()).isOnWhatsapp(entity.isOnWhatsapp())
				.companyId((entity.getCompanyId() == null) ? 0 : entity.getCompanyId().getId()).build();
	}

	public User convertToEntityApprover(UserModel model, User comapnyId, User approverId) {

		String uids = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 6).toUpperCase();

		String password = null;

		if (model.getPassword() == null) {
			password = null;

		} else {
			password = passwordService.encode(model.getPassword());
		}

		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		for (RoleModels tryone : list) {
			roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
		}

		return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
				.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
				.imageUrl(model.getImageUrl()).designation(model.getDesignation()).uid(uids).address(model.getAddress())
				.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
				.individualNo(model.getIndividualNo()).sameASUser(model.getSameASUser())
				.biddingLimit(model.getBiddingLimit()).password(password).status(model.getStatus())
				.isEmailVerifiedTerm(model.isEmailVerifiedTerm()).role(roleList).approverId(approverId)
				.isOnWhatsapp(model.isOnWhatsapp()).phoneCode(model.getPhoneCode()).currency(model.getCurrency())
				.availableOn(model.getAvailableOn()).companyId(comapnyId).build();

	}

	public User convertToEntity(UserModel model) {
		User userCompany = null;

		if (model.getCompanyId() == null || model.getCompanyId() == 0) {
			userCompany = null;

		} else {
			userCompany = findByIds(model.getCompanyId());
		}

		String uids = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "").substring(0, 6).toUpperCase();

		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		for (RoleModels tryone : list) {
			roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
		}

		if (model.getPassword() != null) {
			return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
					.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
					.imageUrl(model.getImageUrl()).designation(model.getDesignation()).uid(uids)// uid(generateUniqueId(model.getCompanyId()))
					.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
					.address(model.getAddress()).biddingLimit(model.getBiddingLimit())
					.password(passwordService.encode(model.getPassword())).status(model.getStatus()).role(roleList)
					.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
					.address(model.getAddress()).biddingLimit(model.getBiddingLimit())
					.individualNo(model.getIndividualNo()).isEmailVerifiedTerm(model.isEmailVerifiedTerm())
					.sameASUser(model.getSameASUser()).password(passwordService.encode(model.getPassword()))
					.status(model.getStatus()).role(roleList).isOnWhatsapp(model.isOnWhatsapp())
					.phoneCode(model.getPhoneCode()).currency(model.getCurrency()).availableOn(model.getAvailableOn())
					.companyId(userCompany).build();

		} else {

			return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
					.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
					.designation(model.getDesignation()).isPhoneVerified(model.isPhoneVerified()).uid(uids)// uid(generateUniqueId(model.getCompanyId()))
					.isEmailVerified(model.isEmailVerified()).status(model.getStatus()).imageUrl(model.getImageUrl())
					.designation(model.getDesignation()).uid(uids).address(model.getAddress())
					.individualNo(model.getIndividualNo()).sameASUser(model.getSameASUser())
					.isPhoneVerified(model.isPhoneVerified()).isEmailVerified(model.isEmailVerified())
					.isEmailVerifiedTerm(model.isEmailVerifiedTerm()).availableOn(model.getAvailableOn())
					.status(model.getStatus()).isOnWhatsapp(model.isOnWhatsapp()).phoneCode(model.getPhoneCode())
					.biddingLimit(model.getBiddingLimit()).currency(model.getCurrency()).type(model.getType())
					.role(roleList).companyId(userCompany).build();

		}

	}

	public User convertToEntitySame(UserModel model) {
		User userCompany = null;

		if (model.getCompanyId() == null || model.getCompanyId() == 0) {
			userCompany = null;

		} else {
			userCompany = findByIds(model.getCompanyId());
		}

		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		for (RoleModels tryone : list) {
			roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
		}

		User approverUser = null;

		if(model.getApproverId()!=null)
		{
			approverUser = findByIds(model.getId());
		}

		return User.builder().id(model.getId()).sameASUser(model.getSameASUser()).name(model.getName())
				.individualNo(model.getIndividualNo()).isOnWhatsapp(model.isOnWhatsapp()).approverId(approverUser)
				.availableOn(model.getAvailableOn()).password(model.getPassword()).primaryPhone(model.getPrimaryPhone())
				.phoneCode(model.getPhoneCode()).currency(model.getCurrency()).biddingLimit(model.getBiddingLimit())
				.designation(model.getDesignation()).email(model.getEmail()).type(model.getType()).uid(model.getUid())
				.companyId(userCompany).role(roleList).isEmailVerified(model.isEmailVerified()).isPhoneVerified(model.isPhoneVerified()).build();

	}

	public User convertToEntityApproverUpdate(UserModel model, User comapnyId, User approverId) {

		List<RoleModels> list = model.getRoleModel();

		List<Role> roleList = new ArrayList<>();

		if(list!=null) {
			for (RoleModels tryone : list) {
				roleList.add(roleDetailsServiceImpl.findByIds(tryone.getId()));
			}
		}

		// if (model.getPassword() != null) {
		return User.builder().id(model.getId()).name(model.getName()).email(model.getEmail()).type(model.getType())
				.primaryPhone(model.getPrimaryPhone()).secondaryPhone(model.getSecondaryPhone()).dob(model.getDob())
				.designation(model.getDesignation()).uid(model.getUid()).isPhoneVerified(model.isPhoneVerified())
				.isEmailVerified(model.isEmailVerified()).biddingLimit(model.getBiddingLimit())
				.individualNo(model.getIndividualNo()).isEmailVerifiedTerm(model.isEmailVerifiedTerm())
				.password(model.getPassword()).status(model.getStatus()).phoneCode(model.getPhoneCode())
				.currency(model.getCurrency()).imageUrl(model.getImageUrl()).address(model.getAddress()).role(roleList)
				.approverId(approverId).isOnWhatsapp(model.isOnWhatsapp()).phoneCode(model.getPhoneCode())
				.currency(model.getCurrency()).sameASUser(model.getSameASUser()).availableOn(model.getAvailableOn())
				.companyId(comapnyId).build();

	}

	public UserEmailPhoneResponseModel convertToModelUserEmailPhoneModel(UserModel model) {
		return UserEmailPhoneResponseModel.builder().id(model.getId()).email(model.getEmail())
				.phoneCode(model.getPhoneCode()).primaryPhone(model.getPrimaryPhone())
				.phoneVerified(model.isPhoneVerified()).isPasswordExist(model.getPassword())
				.emailVerified(model.isEmailVerified()).build();

	}

	public UserListResponseModel convertToUserResponseModel(User entity) {
		return UserListResponseModel.builder().id(entity.getId()).email(entity.getEmail()).name(entity.getName())
				.build();
	}
	
	public EmailRequestModel convertToModelEmailVerification(User entity) {
		
		return EmailRequestModel.builder()
				.email(entity.getEmail()).emailVerified(entity.isEmailVerified()).primaryPhone(entity.getPrimaryPhone())
				.phoneCode(entity.getPhoneCode())
				.build();
		
	}

	public RequestorUpdateModel convaertToRequestorModel(UserModel userModel) {
		return RequestorUpdateModel.builder().id(userModel.getId()).name(userModel.getName())
				.password(passwordService.decode(userModel.getPassword())).email(userModel.getEmail())
				.dob(userModel.getDob()).address(userModel.getAddress()).phoneCode(userModel.getPhoneCode())
				.primaryPhone(userModel.getPrimaryPhone()).imageUrl(userModel.getImageUrl()).build();
	}

	public FinanceEmailAndPhoneNumberResponse convertToResponseFinanceEmailAndPhoneNumber(User entity) {

		return FinanceEmailAndPhoneNumberResponse.builder().id(entity.getId()).email(entity.getEmail())
				.primaryPhone(entity.getPrimaryPhone()).build();

	}

	public void verifyUserByEmailAndPassword(String email, String password)
	{
		User user = userRepository.getUserByEmail(email);
		if(user!=null)
		{
			if(!passwordService.decode(user.getPassword()).equals(password))
			{
				throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,AppConstant.ErrorMessages.EMAIL_CREDENTIALS_MESSAGE);
			}
		}
		if(user==null)
		{
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,AppConstant.ErrorMessages.EMAIL_CREDENTIALS_MESSAGE);
		}
	}

}
package com.athmarine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.RoleMismatchException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.template.EmailTemplate;

@Service
public class CustomerRequesterService {

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public VendorCompanyService vendorCompanyServicel;

	@Autowired
	public OTPService otpService;

	@Autowired
	CustomerCompanyService customerCompanyService;

	public boolean checkAlreadyExists(int id) {
		return customerCompanyRepository.existsById(id);

	}

	public List<UserListResponseModel> addRequester(List<UserModel> userModel) throws MessagingException {

		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();

		for (int i = 0; i < userModel.size(); i++) {
			UserModel RequesterUserModel = userModel.get(i);

			if (!RequesterUserModel.getSameASUser().equals("Yes")) {

				if (userRepository.existsByEmail(RequesterUserModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
							RequesterUserModel.getEmail(), AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}
				if (userRepository.existsByPrimaryPhone(RequesterUserModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							RequesterUserModel.getPrimaryPhone(),
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}
		}

		for (int i = 0; i < userModel.size(); i++) {

			if (!userModel.get(i).getSameASUser().equals("Yes")) {

				String userEmail = userModel.get(i).getEmail();
				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailOTP(userEmail);
					}
				}).start();
			Integer count = userDetailsService.getAllRequestorByCompanyId(userModel.get(i).getCompanyId());
				UserModel findId = userDetailsService
						.convertToModel(userRepository.save(userDetailsService.convertToEntity(userModel.get(i))));
				CustomerCompany customerCompany = customerCompanyService.findByIds(userModel.get(i).getCompanyId());
				customerCompany.setRegistrationStatus(RegistrationStatus.Requester);
				customerCompanyRepository.save(customerCompany);

				findId.setUid(userDetailsService.generateUniqueIdForCustomer(findId.getId(),count+1));
				userDetailsService.updateUserData(findId);
				response.add(convertToModel(findId));
			} else if (userModel.get(i).getSameASUser().equals("Yes")) {
				UserModel bidderModel = userModel.get(i);
				UserModel findId = userDetailsService.updateSameUser(bidderModel);
				response.add(convertToModel(findId));
			}
		}
		return response;
	}

	public UserModel createCustomerRequester(UserModel userModel) throws MessagingException {

		if (!userRepository.existsByEmail(userModel.getEmail())) {

			String token = UUID.randomUUID().toString();

			String otp = String.valueOf(otpService.generateOTP(userModel.getEmail()));

			EmailTemplate template = new EmailTemplate("templates/ResetPasswordEmail.html");
			String url = AppConstant.VerifyRequester.VERIFY_REQUESTER_LINK + "?token= " + token;

			Map<String, String> replacementValues = new HashMap<>();
			replacementValues.put("link", url);
			replacementValues.put("otp", otp);

			String message = template.getTemplate(replacementValues);
			emailService.sendEmail(userModel.getEmail(), AppConstant.VerifyRequester.VERIFY_REQUESTER_SUBJECT_LINE,
					message);

			userModel.setPassword(token);
			User company = null;

			if (userModel.getCompanyId() != null) {

				company = userDetailsService.convertToEntity(userDetailsService.findById(userModel.getCompanyId()),
						company);
			}

			if (userRepository.existsByEmail(userModel.getEmail())) {

				throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
						AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

			}
			// CustomerCompanyModel customerCompanyModel =
			// findById(userModel.getCompanyId());
			UserModel findId = userDetailsService
					.convertToModel(userRepository.save(userDetailsService.convertToEntity(userModel, company)));

			return findId;
		} else {
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.USER_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_ALREADY_EXIST_ERROR_CODE, AppConstant.ErrorMessages.USER_ALREADY_EXIST);
		}

	}

	public CustomerCompanyModel addCreateRequester(CustomerCompanyModel customerCompanyModel) {
		UserModel user = userDetailsService.findById(customerCompanyModel.getUserId());

		if (user.getRoleModel() == null) {
			throw new RoleMismatchException(AppConstant.ErrorTypes.ROLE_EMPTY_ERROR,
					AppConstant.ErrorCodes.ROLE_MISMATCH_ERROR_CODE, AppConstant.ErrorMessages.ROLE_EMPTY_MESSAGE);
		}

		if (!checkAlreadyExists(customerCompanyModel.getUserId())) {
			return convertToModel(customerCompanyRepository.save(convertToEntity(customerCompanyModel)));

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);

	}

	public UserModel updateVerfiedCustomerRequester(UserModel userModel) {

		UserModel findId = userDetailsService.findByEmail(userModel.getEmail());
		if (userRepository.existsByEmail(userModel.getEmail())) {
			findId.setPassword(userModel.getPassword());
			findId.setEmailVerified(true);
			return userDetailsService.updateUserData(findId);
		} else {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE);
		}

	}

// list of all requesters with approved id null
	public List<UserModel> getAllRequesterByCompanyId(@NotBlank Integer id) {
		if (customerCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			List<UserModel> userResponse = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels roe : u.getRoleModel()) {
					if (roe.getName().equals(AppConstant.RoleValues.ROLE_REQUESTER)) {
						if (u.getApproverId() == 0) {
							userResponse.add(u);
						}
					}
				}
			}
			return userResponse;

		} else {
			throw new CustomerCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

// list of all requester 
	public List<UserModel> getAllRequestersByCompanyId(@NotBlank Integer id) {
		if (customerCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			List<UserModel> userResponse = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels role : u.getRoleModel()) {
					if (role.getName().equals(AppConstant.RoleValues.ROLE_REQUESTER)) {

						userResponse.add(u);

					}
				}
			}
			return userResponse;

		} else {
			throw new CustomerCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public CustomerCompanyModel deleteCustomerRequester(Integer id) {
		CustomerCompanyModel customerCompany = findById(id);

		customerCompany.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
		return convertToModel(customerCompanyRepository.save(convertToEntity(customerCompany)));

	}

	public CustomerCompanyModel findById(@NotBlank Integer id) {
		if (id == null) {
			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(customerCompanyRepository.findById(id).orElseThrow(
					() -> new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
							AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE)));
		}
	}

	public UserModel findCustomerRequesterById(@NotBlank int id) {

		CustomerCompanyModel customerCompanyModel = findById(id);

		customerCompanyModel.setId(id);

		UserModel user = userDetailsService.findById(customerCompanyModel.getUserId());

		if (user.getRoleModel() != null) {
			// if
			// (user.getRoleModel().getName().equals(AppConstant.RoleValues.ROLE_REQUESTER))
			// {
			return user;
		} else {
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);
		}
//		} else {
//			throw new RoleMismatchException(AppConstant.ErrorTypes.ROLE_EMPTY_ERROR,
//					AppConstant.ErrorCodes.ROLE_MISMATCH_ERROR_CODE, AppConstant.ErrorMessages.ROLE_EMPTY_MESSAGE);
//		}

	}

	public List<UserModel> getAllRequsterByCompanyId(@NotBlank Integer id) {
		if (customerCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			List<UserModel> userResponse = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels role : u.getRoleModel()) {
					if (role.getName().equals(AppConstant.RoleValues.ROLE_REQUESTER)) {
						if (u.getApproverId() == 0) {
							userResponse.add(u);
						}
					}
				}
			}
			return userResponse;

		} else {
			throw new CustomerCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	private CustomerCompany convertToEntity(CustomerCompanyModel model) {

		return CustomerCompany.builder().id(model.getId())
				.userCustomer(userDetailsService.findUserEntityById(model.getUserId())).logo(model.getLogo())
				.address(model.getAddress()).city(model.getCity()).pincode(model.getPincode()).faxno(model.getFaxno())
				.isAdminRegistered(model.isAdminRegistered()).isRegistered(model.isAdminRegistered())
				.status(model.getStatus()).registrationNo(model.getRegistrationNo()).build();
	}

	private CustomerCompanyModel convertToModel(CustomerCompany entity) {
		return CustomerCompanyModel.builder().id(entity.getId()).userId(entity.getId()).logo(entity.getLogo())
				.address(entity.getAddress()).city(entity.getCity()).pincode(entity.getPincode())
				.faxno(entity.getFaxno()).isAdminRegistered(entity.isAdminRegistered()).status(entity.getStatus())
				.isRegistered(entity.isAdminRegistered()).registrationNo(entity.getRegistrationNo()).build();
	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}

}
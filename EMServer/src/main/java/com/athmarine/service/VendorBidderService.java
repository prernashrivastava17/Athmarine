package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.RoleMismatchException;
import com.athmarine.exception.VendorApproverNotFoundException;
import com.athmarine.exception.VendorBidderNotFoundException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.request.VendorCompanyModel;
import com.athmarine.resources.AppConstant;

@Service
public class VendorBidderService {

	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MasterStateService masterStateService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public VendorCompanyService vendorCompanyServicel;

	@Autowired
	public OTPService otpService;

	public List<UserListResponseModel> addVendorBidder(List<UserModel> userModel) throws MessagingException {

		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();
		for (int i = 0; i < userModel.size(); i++) {
			UserModel bidderModel = userModel.get(i);
			if (!userModel.get(i).getSameASUser().equals("Yes")) {
				if (bidderModel.getCompanyId() == 0) {

					throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
							AppConstant.ErrorCodes.ID_NULL_ERROR_CODE,
							"Company " + AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);

				}

				else if (userRepository.existsByEmail(bidderModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR, bidderModel.getEmail(),
							bidderModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				} else if (userRepository.existsByPrimaryPhone(bidderModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							bidderModel.getPrimaryPhone(), bidderModel.getPrimaryPhone() + " "
									+ AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}

		}

		for (int i = 0; i < userModel.size(); i++) {

			if (userModel.get(i).getSameASUser().equals("No")) {
				Integer count = userDetailsService.getAllBidderByCompanyId(userModel.get(i).getCompanyId());

				String userEmail = userModel.get(i).getEmail();
				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailVerificationLink(userEmail);
					}
				}).start();
				UserModel findId = userDetailsService
						.convertToModel(userRepository.save(userDetailsService.convertToEntity(userModel.get(i))));
				VendorCompany vendorCompany = vendorCompanyServicel.findByIds(userModel.get(i).getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Bidder);
				vendorCompanyRepository.save(vendorCompany);

				findId.setUid(userDetailsService.generateUniqueIdVendor(findId.getId(), count + i + 1));
			     userDetailsService.updateUserData(findId);
				response.add(convertToModel(findId));

			} else if (userModel.get(i).getSameASUser().equals("Yes")) {
				UserModel bidderModel = userModel.get(i);
				UserModel vendorRegistrationStatus=userDetailsService.updateSameUser(bidderModel);
				VendorCompany vendorCompany = vendorCompanyServicel.findByIds(vendorRegistrationStatus.getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Bidder);
				vendorCompanyRepository.save(vendorCompany);
			}
		}

		return response;

	}

	public List<UserListResponseModel> addbidders(List<UserModel> userModel) {

		List<UserModel> extistingList = userModel.stream().filter(es -> userRepository.existsByEmail(es.getEmail()))
				.filter(es -> userRepository.existsByEmail(es.getEmail())).collect(Collectors.toList());

		List<User> user = new ArrayList<>();
		if (extistingList.size() > 0) {

			extistingList.stream().map(es -> es.getEmail()).collect(Collectors.toList());
		} else {
			user = userModel.stream().map(e -> userDetailsService.convertToEntity(e)).collect(Collectors.toList());
		}

		List<User> userEntityList = user.stream().map(r -> userRepository.save(r)).collect(Collectors.toList());

		return userEntityList.stream()
				.map(e -> UserListResponseModel.builder().name(e.getName()).email(e.getEmail()).build())
				.collect(Collectors.toList());

	}

	public UserModel updateVendorBidder(UserModel userModel) {

		return userDetailsService
				.convertToModel(userRepository.save(userDetailsService.convertToEntity(userModel, null)));
	}

	public List<UserListResponseModel> updateAllVendorBidder(List<UserModel> userModel) {
		List<UserListResponseModel> responseModel = new ArrayList<UserListResponseModel>();
		for (int i = 0; i < userModel.size(); i++) {
			UserModel user = userDetailsService.findById(userModel.get(i).getId());
			userModel.get(i).setId(user.getId());
			userModel.get(i).setUid(user.getUid());
			responseModel.add(convertToModel(userDetailsService.updateUserData(userModel.get(i))));
		}
		return responseModel;

	}

	public VendorCompanyModel findVendorBidderById(@NotBlank int id) {

		VendorCompanyModel vendorCompany = findById(id);

		vendorCompany.setId(id);

		UserModel user = userDetailsService.findById(vendorCompany.getUserId());

		List<RoleModels> listOfRoleModel = user.getRoleModel();

		if (listOfRoleModel != null) {
			for (RoleModels role : listOfRoleModel) {
				if (role.getName().equals(AppConstant.RoleValues.ROLE_BIDDER)) {
					return vendorCompany;
				} else {
					throw new VendorBidderNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST);
				}
			}
		} else {
			throw new RoleMismatchException(AppConstant.ErrorTypes.ROLE_EMPTY_ERROR,
					AppConstant.ErrorCodes.ROLE_MISMATCH_ERROR_CODE, AppConstant.ErrorMessages.ROLE_EMPTY_MESSAGE);
		}
		return vendorCompany;

	}

	public VendorCompanyModel findById(@NotBlank Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return vendorCompanyServicel.convertToModel(vendorCompanyRepository.findById(id).orElseThrow(
					() -> new VendorApproverNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST)));
		}

	}

	public List<UserListResponseModel> getAllBidderByCompanyId(@NotBlank Integer id) {
		if (vendorCompanyServicel.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllVendorUserByCompanyId(id);
			List<UserListResponseModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels role : u.getRoleModel()) {
					if (role.getName().equals(AppConstant.RoleValues.ROLE_BIDDER)) {
						if (u.getApproverId() == 0) {
							UserListResponseModel userResponse = convertToModel(u);
							userResponseList.add(userResponse);
						}
					}
				}
			}
			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> getAllBidderByCompanyId2(@NotBlank Integer id) {
		if (vendorCompanyServicel.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllVendorUserByCompanyId(id);
			List<UserModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				if (u.getRoleModel().getClass().getName().equals(AppConstant.RoleValues.ROLE_BIDDER)) {
					if (u.getApproverId() == 0) {
						userResponseList.add(u);
					}
				}
			}
			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> getAllBidderByCMPId(@NotBlank Integer id) {
		if (vendorCompanyServicel.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllVendorUserByCompanyId(id);
			List<UserModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels roe : u.getRoleModel()) {
					if (roe.getName().equals(AppConstant.RoleValues.ROLE_BIDDER)) {

						userResponseList.add(u);

					}
				}
			}
			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> getAllBidderByAproverId(@NotBlank Integer id) {
		UserModel approver = userDetailsService.findById(id);
		if (vendorCompanyServicel.checkAlreadyExists(approver.getCompanyId())) {
			List<UserModel> userList = userDetailsService.getAllVendorUserByCompanyId(approver.getCompanyId());
			List<UserModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels role : u.getRoleModel()) {
					if (role.getName().equals(AppConstant.RoleValues.ROLE_BIDDER)) {
						if (u.getApproverId().equals(id)) {

							userResponseList.add(u);
						}
					}
				}
			}
			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public boolean checkAlreadyExists(int id) {
		return vendorCompanyRepository.existsById(id);

	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}

}
package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerApprover;
import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.CustomerApproverRepository;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.CustomerApproverModel;
import com.athmarine.request.CustomerHeadModel;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class CustomerHeadService {

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	CustomerCompanyService companyService;

	@Autowired
	CustomerApproverRepository customerApproverRepository;

	@Autowired
	public EmailService emailService;

	@Autowired
	public CustomerApproverService customerApproverService;

	@Autowired
	public OTPService otpService;

	public boolean checkAlreadyExists(int id) {
		return customerCompanyRepository.existsById(id);

	}

	public List<UserListResponseModel> addCustomerHead(List<CustomerHeadModel> customerHeadModel) {

		for (int i = 0; i < customerHeadModel.size(); i++) {
			UserModel HeadUserModel = customerHeadModel.get(i).getUserModel();
				if (userRepository.existsByEmail(HeadUserModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR, HeadUserModel.getEmail(),
							AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}
				if (userRepository.existsByPrimaryPhone(HeadUserModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							HeadUserModel.getPrimaryPhone(),
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
		}	
		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();
		for (int j = 0; j < customerHeadModel.size(); j++) {
			UserModel headModel = customerHeadModel.get(j).getUserModel();

				List<UserModel> approverList = customerHeadModel.get(j).getApproverModel();

				CustomerApproverModel customerApproverModel = customerHeadModel.get(j).getCustomerApproverModel();

				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailOTP(headModel.getEmail());
					}
				}).start();
//			Integer count = userDetailsService.getCustomerCountByComapanyId(headModel.getCompanyId(),
//					headModel.getRoleModel().getId());
				UserModel findId = userDetailsService
						.convertToModel(userRepository.save(userDetailsService.convertToEntity(headModel)));
				User findId2 = userDetailsService.convertToEntity(findId);
				List<User> approversList = new ArrayList<User>();
				if (approverList.size() > 0) {
					for (int i = 0; i < approverList.size(); i++) {
						User approver = userDetailsService.findByIds(approverList.get(i).getId());
						approver.setApproverId(findId2);
						approversList.add(approver);
						customerApproverModel.setUserId(approverList.get(i).getId());
						customerApproverModel.setApprovedBy(findId);
						customerApproverRepository.save(convertToEntity(customerApproverModel));
					}

					userDetailsService.updateUserDataCustomer(approversList);
				}
				CustomerCompany customerCompany = companyService
						.findByIds(customerHeadModel.get(j).getUserModel().getCompanyId());
				customerCompany.setRegistrationStatus(RegistrationStatus.Company_Head);
				customerCompanyRepository.save(customerCompany);

				findId.setUid(userDetailsService.generateUniqueIdForCustomer(findId.getId(), 0 + j + 1));
				userDetailsService.updateUserData(findId);
				response.add(convertToModel(findId));
			}
		return response;
	}

	public List<UserModel> getAllApproverByCompanyId(@NotBlank Integer id) {
		if (companyService.checkAlreadyExists(id)) {
			//List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			//User company = userDetailsService.findByIds(id);
			List<UserModel> userResponse = new ArrayList<>();
//			for (int i = 0; i < userList.size(); i++) {
//				User u = userDetailsService.convertToEntity(userList.get(i), company);
//				if (u.getRole().getName().equals(AppConstant.RoleValues.ROLE_APPROVER)
//						|| u.getRole().getName().equals(AppConstant.RoleValues.ROLE_BIDDER_APPROVER)) {
//
//					if (!customerApproverRepository.existsById(u.getId())) {
//						userResponse.add(userDetailsService.convertToModel(u));
//					}
//				}
//			}
			return userResponse;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> getAllApproverByHeadId(@NotBlank Integer id) {
		UserModel head = userDetailsService.findById(id);
		if (companyService.checkAlreadyExists(head.getCompanyId())) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(head.getCompanyId());
			List<UserModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for(RoleModels roe:u.getRoleModel()) {
				if (roe.getName().equals(AppConstant.RoleValues.ROLE_APPROVER)) {
					if (u.getApproverId().equals(id)) {

						userResponseList.add(u);
					}
				}}
			}
			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<CustomerHeadModel> getAllManagingHeadByCompanyId(@NotBlank Integer id) {
		if (companyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			List<UserModel> headList = new ArrayList<UserModel>();
			List<CustomerHeadModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for(RoleModels roe:u.getRoleModel()) {
				if (roe.getName().equals(AppConstant.RoleValues.ROLE_HEAD)) {

					headList.add(u);

				}}
			}
			CustomerHeadModel HeadModel = new CustomerHeadModel();
			for (Integer j = 0; j < headList.size(); j++) {

				HeadModel.setUserModel(headList.get(j));
				List<UserModel> approver = getAllApproverByHeadId(headList.get(j).getId());
				if (approver.size() == 0) {
					continue;
				}

				HeadModel.setApproverModel(approver);
				userResponseList.add(HeadModel);
				HeadModel = new CustomerHeadModel();
			}

			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<UserModel> demo(int id) {
		User user = userDetailsService.findByIds(id);
		return user.getCustomerApprovers().stream()
				.map(a -> userDetailsService.convertToModel(a.getUserCustomerApprover())).collect(Collectors.toList());
	}

	private CustomerApprover convertToEntity(CustomerApproverModel model) {

		return CustomerApprover.builder().id(model.getId())
				.userCustomerApprover(userDetailsService.findUserEntityById(model.getUserId()))
				.isApprovedStatus(model.getIsApprovedStatus()).status(model.getStatus())
				.approvedBy(userDetailsService.findUserEntityById(model.getApprovedBy().getId())).build();

	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}

}
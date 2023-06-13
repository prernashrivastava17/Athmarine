package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerApprover;
import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.CustomerApproverRepository;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.CustomerApproverModel;
import com.athmarine.request.CustomerApproverUserModel;
import com.athmarine.request.ResponseModel;
import com.athmarine.request.RoleModels;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class CustomerApproverService {

	@Autowired
	CustomerApproverRepository customerApproverRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerCompanyService customerCompanyService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public OTPService otpService;

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	public List<UserListResponseModel> saveCustomerApproverDetails(
			List<CustomerApproverUserModel> customerApproverUserModel) {

		for (int i = 0; i < customerApproverUserModel.size(); i++) {
			UserModel approverUserModel = customerApproverUserModel.get(i).getApprover();

			if (!approverUserModel.getSameASUser().equals("Yes")) {

				if (userRepository.existsByEmail(approverUserModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
							approverUserModel.getEmail(), AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}
				if (userRepository.existsByPrimaryPhone(approverUserModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							approverUserModel.getPrimaryPhone(),
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}
		}
		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();

		for (Integer j = 0; j < customerApproverUserModel.size(); j++) {

			UserModel approverUser = customerApproverUserModel.get(j).getApprover();

			if (!approverUser.getSameASUser().equals("Yes")) {

				String approverString = approverUser.getEmail();
				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailOTP(approverString);
					}
				}).start();
//			Integer count = userDetailsService.getCustomerCountByComapanyId(approverUser.getCompanyId(),
//					approverUser.getRoleModel().getId());

				approverUser = userDetailsService.createUser(approverUser);
				User findId = userDetailsService.findByIds(approverUser.getId());

				List<UserModel> requesterIds = customerApproverUserModel.get(j).getRequesterIds();

				CustomerApproverModel customerApproverModel = customerApproverUserModel.get(j)
						.getCustomerApproverModel();
				customerApproverModel.setId(null);
				customerApproverModel.setUserId(approverUser.getId());
				customerApproverModel.setApprovedBy(approverUser);
				List<User> requestorList = new ArrayList<User>();
				if (requesterIds.size() > 0) {
					for (Integer i = 0; i < requesterIds.size(); i++) {
						User requester = userDetailsService.findByIds(requesterIds.get(i).getId());
						requester.setApproverId(findId);
						requestorList.add(requester);
					}
					userDetailsService.updateUserDataCustomer(requestorList);
				}

				CustomerCompany customerCompany = customerCompanyService
						.findByIds(customerApproverUserModel.get(0).getApprover().getCompanyId());
				customerCompany.setRegistrationStatus(RegistrationStatus.Approver);
				customerCompanyRepository.save(customerCompany);
				approverUser.setUid(userDetailsService.generateUniqueIdForCustomer(approverUser.getId(), 0 + j + 1));
				userDetailsService.updateUserData(approverUser);
				response.add(convertToModel(approverUser));
			} else if (approverUser.getSameASUser().equals("Yes")) {
				UserModel bidderModel = approverUser;
				UserModel findId = userDetailsService.updateSameUser(bidderModel);
				response.add(convertToModel(findId));
			}
		}
		return response;
	}

	public boolean checkAlreadyExists(int id) {
		return customerApproverRepository.existsById(id);

	}

	public CustomerApproverModel findById(@NotBlank Integer id) {

		if (id == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_APPROVER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_APPROVER_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(customerApproverRepository.findById(id).orElseThrow(
					() -> new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_APPROVER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.CUSTOMER_APPROVER_ERROR_CODE,
							AppConstant.ErrorMessages.CUSTOMER_APPROVER_NOT_EXIST_MESSAGE)));
		}
	}

	public CustomerApproverModel getCustomerApproverDetails(@NotNull int id) {

		return convertToModel(customerApproverRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_APPROVER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.CUSTOMER_APPROVER_ERROR_CODE,
						AppConstant.ErrorMessages.CUSTOMER_APPROVER_NOT_EXIST_MESSAGE)));

	}

	public List<UserModel> getAllApproverByHeadId(@NotNull int id) {

		return userDetailsService.getAllCustomerByApproverId(id);
	}

	public List<ResponseModel> getAllApproverByCompanyId(@NotBlank Integer id) {
		List<ResponseModel> response = new ArrayList<ResponseModel>();

		if (customerCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
			List<UserModel> userResponse = new ArrayList<>();
			
			for(UserModel approver:userList) {
					UserModel u = approver;
					for (RoleModels role : u.getRoleModel()) {
						if (role.getName().equals(AppConstant.RoleValues.ROLE_APPROVER)) {

							userResponse.add(u);
						}
					}
				}

			for (Integer j = 0; j < userResponse.size(); j++) {
				ResponseModel mod = new ResponseModel();

				UserModel u = userResponse.get(j);

				mod.setApprover(u);

				List<UserModel> uid = userDetailsService.findAllByApproverId(u.getId());
				List<UserListResponseModel> requestorList = uid.stream().map(requstor -> convertToModel(requstor))
						.collect(Collectors.toList());
				mod.setRequesterIds(requestorList);
				response.add(mod);

			}
			return response;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public CustomerApproverModel deleteCustomerApprovereDetails(Integer id) {

		CustomerApproverModel customerApprover = findById(id);

		customerApprover.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(customerApproverRepository.save(convertToEntity(customerApprover)));
	}

	public CustomerApproverModel updateCustomerApprover(CustomerApproverModel customerApproverModel) {

//		UserModel user = userDetailsService.findById(customerApproverModel.getId());
//
//		UserModel userApprovedBy = userDetailsService.findById(customerApproverModel.getApprovedBy().getId());

//		if (user.getRoleModel().getName().equals(AppConstant.RoleValues.ROLE_APPROVER)
//				&& userApprovedBy.getRoleModel().getName().equals(AppConstant.RoleValues.ROLE_ADMIN)) {
//			if (userApprovedBy.getRoleModel().getName().equals(AppConstant.RoleValues.ROLE_ADMIN)) {
//				customerApproverModel.setId(user.getId());
//
//				return convertToModel(customerApproverRepository.save(convertToEntity(customerApproverModel)));
//
//			} else if (userApprovedBy.getId() == user.getId()) {
//
//				customerApproverModel.setId(user.getId());
//
//				return convertToModel(customerApproverRepository.save(convertToEntity(customerApproverModel)));
//			} else {
//
//				throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
//						AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE,
//						AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);
//
//			}
//
//		} else {
//			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
//					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);
//		}
		return null;

	}

	private CustomerApprover convertToEntity(CustomerApproverModel model) {

		return CustomerApprover.builder().id(model.getId())
				.userCustomerApprover(userDetailsService.findUserEntityById(model.getUserId()))
				.isApprovedStatus(model.getIsApprovedStatus()).status(model.getStatus())
				.approvedBy(userDetailsService.findUserEntityById(model.getApprovedBy().getId())).build();

	}

	private CustomerApproverModel convertToModel(CustomerApprover entity) {

		return CustomerApproverModel.builder().id(entity.getId()).userId(entity.getId())
				.approvedBy(userDetailsService.convertToModel(entity.getApprovedBy())).status(entity.getStatus())
				.isApprovedStatus(entity.getIsApprovedStatus()).build();

	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}
}
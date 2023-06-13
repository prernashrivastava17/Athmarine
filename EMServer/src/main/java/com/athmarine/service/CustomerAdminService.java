package com.athmarine.service;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.MSTState;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class CustomerAdminService {

	@Autowired
	private CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	public CustomerCompanyService customerCompanyService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public OTPService otpService;

	@Autowired
	public EmailService emailService;

	@Autowired
	MasterStateService masterStateService;

	public UserModel createCustomerAdmin(UserModel userModel) throws MessagingException {
		if (!userRepository.existsByEmail(userModel.getEmail())) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					otpService.createEmailOTP(userModel.getEmail());
				}
			}).start();

			if (userRepository.existsByEmail(userModel.getEmail())) {

				throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
						AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

			}
			if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {

				throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
						AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

			}
			Integer count = userDetailsService.getAllCustomerAdminByCompanyId(userModel.getCompanyId());
			UserModel findId = userDetailsService.createUser(userModel);
			CustomerCompany customerCompany = customerCompanyService.findByIds(findId.getCompanyId());
			customerCompany.setRegistrationStatus(RegistrationStatus.Admin);
			customerCompanyRepository.save(customerCompany);

			findId.setUid(userDetailsService.generateUniqueIdForCustomer(findId.getId(), count+1));
			 System.out.println("count"+findId.getUid());
			UserModel updateduserodel = userDetailsService.updateUserData(findId);
			return updateduserodel;
		} else {

			new Thread(new Runnable() {

				@Override
				public void run() {
					otpService.createEmailOTP(userModel.getEmail());
				}
			}).start();
			throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
					userModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_ALREADY_EXIST);
		}

	}

	public CustomerCompanyModel addCustomerAdmin(CustomerCompanyModel customerCompanyModel) {

		if (!checkAlreadyExists(customerCompanyModel.getUserId())) {

			MSTState state = null;

			if (customerCompanyModel.getMstateStateModel() != null) {

				state = masterStateService.findByIds(customerCompanyModel.getMstateStateModel().getId());
			}

			return customerCompanyService.convertToModel(customerCompanyRepository
					.save(customerCompanyService.convertToEntity(customerCompanyModel, state)));

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.ADMIN_ALREADY_EXIST_ERROR,
					AppConstant.ErrorCodes.ADMIN_ALREADY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.ADMIN_ALREADY_EXIST);

	}

	public CustomerCompanyModel updateCustomerAdmin(CustomerCompanyModel customerCompanyModel) {

		CustomerCompanyModel customerCompany = findById(customerCompanyModel.getId());

		customerCompany.setId(customerCompanyModel.getId());

		MSTState state = null;

		if (customerCompanyModel.getMstateStateModel() != null) {

			state = masterStateService.findByIds(customerCompanyModel.getMstateStateModel().getId());
		}
		return customerCompanyService.convertToModel(
				customerCompanyRepository.save(customerCompanyService.convertToEntity(customerCompanyModel, state)));
	}

	public UserModel findCustomerAdminById(@NotBlank int id) {

		UserModel admin = userDetailsService.findById(id);

		return admin;

	}

	public CustomerCompanyModel deleteCustomerAdmin(Integer id) {
		CustomerCompanyModel vendorCompany = findById(id);

		vendorCompany.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
		MSTState state = null;

		if (vendorCompany.getMstateStateModel() != null) {

			state = masterStateService.findByIds(vendorCompany.getMstateStateModel().getId());
		}
		return customerCompanyService.convertToModel(
				customerCompanyRepository.save(customerCompanyService.convertToEntity(vendorCompany, state)));

	}

	public boolean checkAlreadyExists(int id) {
		return customerCompanyRepository.existsById(id);

	}

	public CustomerCompanyModel findById(@NotBlank Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return customerCompanyService.convertToModel(customerCompanyRepository.findById(id)
					.orElseThrow(() -> new CustomerCompanyNotFoundException(
							AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
							AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE)));
		}

	}

}

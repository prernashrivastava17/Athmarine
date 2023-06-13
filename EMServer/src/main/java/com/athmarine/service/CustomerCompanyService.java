package com.athmarine.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.MSTCountry;
import com.athmarine.entity.MSTState;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.VendorCompany;
import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.request.MasterStateModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.util.JwtTokenUtil;

@Service
public class CustomerCompanyService {

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private MasterStateService masterStateService;

	@Autowired
	private VendorCompanyService vendorCompanyService;

	@Autowired
	private JwtTokenUtil accessToken;

	@Autowired
	private MasterCountryService masterCountryService;

	@Autowired
	VesselService vesselService;

	public boolean checkAlreadyExists(int id) {
		return customerCompanyRepository.existsById(id);

	}

	public CustomerCompanyModel createCustomerComapny(UserCustomerModel userCustomerModel) {

		UserModel userModel = userCustomerModel.getUserModel();

		CustomerCompanyModel customerCompanyModel = userCustomerModel.getCustomerCompanyModel();
		MSTState state = null;

		if (customerCompanyModel.getMstateStateModel() != null) {

			state = masterStateService.findByIds(customerCompanyModel.getMstateStateModel().getId());
		}
		userModel = userDetailsService.createCompany(userModel);
		customerCompanyModel.setId(null);
		customerCompanyModel.setUserId(userModel.getId());
		customerCompanyModel.setRegistrationStatus(RegistrationStatus.Address);
		return saveCustomerCompanyDetails(customerCompanyModel, state);

	}

	public CustomerCompanyModel saveCustomerCompanyDetails(CustomerCompanyModel customerCompanyModel, MSTState state) {

		if (!checkAlreadyExists(customerCompanyModel.getUserId())) {
			CustomerCompany entity = convertToEntity(customerCompanyModel, state);
			entity.setReferralUseCount(0);
			entity = customerCompanyRepository.save(entity);

			entity.setReferralCode(generateReferralCode(entity.getId()));
			CustomerCompanyModel companyModel = convertToModel(customerCompanyRepository.save(entity));

			CustomerCompany findId = findByIds(companyModel.getId());
			findId.setId(companyModel.getId());
			findId.setCompanyUid(generateCompanyUID(findId.getId()));
			customerCompanyRepository.save(findId);
			return companyModel;
		} else {
			CustomerCompany customerCompany = findByIds(customerCompanyModel.getUserId());
			customerCompanyModel.setId(customerCompany.getId());
			customerCompanyModel.setReferralCode(customerCompany.getReferralCode());
			customerCompanyModel.setCompanyUid(customerCompany.getCompanyUid());
			return updatedCustomerCompany(customerCompanyModel);
		}
	}

	public CustomerCompanyModel updatedCustomerCompany(CustomerCompanyModel customerCompany) {
		MSTState state = null;

		if (customerCompany.getMstateStateModel() != null) {

			state = masterStateService.findByIds(customerCompany.getMstateStateModel().getId());

		}
		customerCompany.setId(customerCompany.getUserId());
		customerCompany.setCompanyUid(customerCompany.getCompanyUid());
		customerCompany.setReferralCode(customerCompany.getReferralCode());
		return convertToModel(customerCompanyRepository.save(convertToEntity(customerCompany, state)));

	}

	public CustomerCompanyModel findById(@NotBlank Integer id) {

		if (id == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(customerCompanyRepository.findById(id).orElseThrow(
					() -> new UserNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
							AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE)));
		}
	}

	public CustomerCompany findByIds(Integer id) {
		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		}

		return customerCompanyRepository.findById(id)
				.orElseThrow(() -> new CustomerCompanyNotFoundException(
						AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
						AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE));
	}

	public CustomerCompanyModel customerCompanyData(int id) {
		return convertToModel(customerCompanyRepository.findById(id)
				.orElseThrow(() -> new VendorCompanyNotFoundException(
						AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
						AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE)));
	}

	public CustomerCompanyModel deleteCustomerCompany(Integer id) {

		CustomerCompanyModel customer = findById(id);

		customer.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		MSTState state = null;

		if (customer.getMstateStateModel() != null) {

			state = masterStateService.findByIds(customer.getMstateStateModel().getId());
		}

		return convertToModel(customerCompanyRepository.save(convertToEntity(customer, state)));
	}

	public CustomerCompanyModel updateCustomerCompany(CustomerCompanyModel customerCompanyModel) {

		CustomerCompanyModel customerCompany = findById(customerCompanyModel.getId());
		customerCompanyModel.setId(customerCompany.getId());
		customerCompanyModel.setAddress(customerCompany.getAddress());
		customerCompanyModel.setStatus(customerCompany.getStatus());
		customerCompanyModel.setPincode(customerCompany.getPincode());
		customerCompanyModel.setReferralCodeUsed(customerCompany.getReferralCodeUsed());
		customerCompanyModel.setReferralCode(customerCompany.getReferralCode());
		customerCompanyModel.setRegistrationStatus(RegistrationStatus.Business_Information);

		MSTState state = null;

		if (customerCompanyModel.getMstateStateModel() != null) {

			state = masterStateService.findByIds(customerCompanyModel.getMstateStateModel().getId());
		}

		return convertToModel(customerCompanyRepository.save(convertToEntity(customerCompanyModel, state)));

	}

	public UserCustomerModel getCustomerCompany(int id) {
		UserCustomerModel model = new UserCustomerModel();
		model.setCustomerCompanyModel(findById(id));
		model.setUserModel(userDetailsService.findById(id));
		return model;
	}

	public void validateReferralCode(String referralCode) {

		if (customerCompanyRepository.existsByReferralCode(referralCode)) {
		} else {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.REFERRAL_CODE_INCORRECT_ERROR,
					AppConstant.ErrorCodes.WRONG_REFERRAL_CODE_ERROR_CODE,
					AppConstant.ErrorMessages.WRONG_REFERRAL_CODE_MESSAGE);
		}
	}

	public String generateReferralCode(Integer id) {

		StringBuilder referralCode = new StringBuilder("C");
		CustomerCompany customerCompany = findByIds(id);
		referralCode.append(customerCompany.getCountryId().getShortName());
		referralCode.append(LocalDateTime.now().getYear() % 2000);
		referralCode.append(String.format("%04d", id));

		return referralCode.toString();
	}

	public String generateCompanyUID(Integer model) {
		CustomerCompany company = findByIds(model);

		StringBuilder referralCode = new StringBuilder("C");
		// VendorCompany vendorCompany = findByIds(id);
		MSTCountry country = masterCountryService.findByCountryId(company.getCountryId().getId());
		referralCode.append(country.getShortName());
		// CustomerCompany company =
		// customerCompanyRepository.findFirstByOrderByIdDesc();

		LocalDateTime time = company.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		if (time.getYear() == LocalDateTime.now().getYear()) {

			referralCode.append(LocalDateTime.now().getYear() % 2000);
			referralCode.append(String.format("%04d", company.getId()));

			// referralCode.append(String.format("%04d", Integer.parseInt(lastCount) + 1));
		} else {
			referralCode.append(LocalDateTime.now().getYear() % 2000);
			// String lastCount =
			// company.getCompanyUid().substring(company.getCompanyUid().length() - 4);
		}

		return referralCode.toString();
	}

	public String generateCustomerUId(Integer id) {

		StringBuilder uniqueCode = new StringBuilder("C");
		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
		uniqueCode.append(vendorCompany.getCountryId().getShortName());
		uniqueCode.append(LocalDateTime.now().getYear() % 2000);
		uniqueCode.append(String.format("%04d", id));

		// String role = userDetailsServiceImpl.findById(id).getRoleModel().getName();
		String role = "Admin";
		if (role.equalsIgnoreCase("Admin")) {
			uniqueCode.append("D");
			uniqueCode.append(LocalDateTime.now().getYear() % 2000);
			uniqueCode.append(String.format("%02d", id));
		}
		if (role.equalsIgnoreCase("Requester")) {
			uniqueCode.append("R");
			uniqueCode.append(LocalDateTime.now().getYear() % 2000);
			uniqueCode.append(String.format("%02d", id));
		}
		if (role.equalsIgnoreCase("Approver")) {
			uniqueCode.append("A");
			uniqueCode.append(LocalDateTime.now().getYear() % 2000);
			uniqueCode.append(String.format("%02d", id));
		}
		if (role.equalsIgnoreCase("Head")) {
			uniqueCode.append("M");
			uniqueCode.append(LocalDateTime.now().getYear() % 2000);
			uniqueCode.append(String.format("%02d", id));
		}
		if (role.equalsIgnoreCase("Finance")) {
			uniqueCode.append("F");
			uniqueCode.append(LocalDateTime.now().getYear() % 2000);
			uniqueCode.append(String.format("%02d", id));
		}
		return uniqueCode.toString();
	}

	CustomerCompany convertToEntity(CustomerCompanyModel model, MSTState state) {

		return CustomerCompany.builder().id(model.getId())
				.userCustomer(userDetailsService.findUserEntityById(model.getUserId())).logo(model.getLogo())
				.address(model.getAddress()).city(model.getMasterCity()).pincode(model.getPincode())
				.faxno(model.getFaxno()).isAdminRegistered(model.isAdminRegistered())
				.isRegistered(model.isAdminRegistered()).yearOfReg(model.getYearOfReg()).status(model.getStatus())
				.registrationNo(model.getRegistrationNo()).stateId(state).companyUid(model.getCompanyUid())
				.countryId(masterCountryService.findByCountryId(model.getMasterCountryModel().getId()))
				.isRegisteredSuccessfully(model.isRegisteredSuccessfully())// .vessel(vesselService.findAllByVesselId(model.getVessel()))
				.registrationStatus(model.getRegistrationStatus()).referralCodeUsed(model.getReferralCodeUsed())
				.referralCode(model.getReferralCode()).cityCustomer(model.getCity())
				.country_of_registration(model.getCountry_of_registration())
				.city_of_registration(model.getCity_of_registration()).build();

	}

	CustomerCompanyModel convertToModel(CustomerCompany entity) {
		UserModel userModel = userDetailsService.findById(entity.getUserCustomer().getId());

		MasterStateModel masterState;
		Integer masterStateModel = (entity.getStateId() == null) ? 0 : entity.getStateId().getId();

		masterState = (masterStateModel == 0) ? null : masterStateService.findById(masterStateModel);

		return CustomerCompanyModel.builder().id(entity.getId()).userId(entity.getId()).logo(entity.getLogo())
				.address(entity.getAddress()).city(entity.getCity()).pincode(entity.getPincode())
				.faxno(entity.getFaxno()).isAdminRegistered(entity.isAdminRegistered()).status(entity.getStatus())
				.yearOfReg(entity.getYearOfReg()).isRegistered(entity.isAdminRegistered())
				.registrationNo(entity.getRegistrationNo()).masterCity(entity.getCity()).mstateStateModel(masterState)
				.token(accessToken.generateToken(userModel.getEmail()))
				.isRegisteredSuccessfully(entity.isRegisteredSuccessfully())
				.registrationStatus(entity.getRegistrationStatus()).referralCode(entity.getReferralCode())
				.referralCodeUsed(entity.getReferralCodeUsed()).city_of_registration(entity.getCity_of_registration())
				.country_of_registration(entity.getCountry_of_registration())
				.masterCountryModel(masterCountryService.findByIds(entity.getCountryId().getId())).build();

	}

	public String generateReferalCode(CustomerCompanyModel model) {
		StringBuilder referalCodeC = new StringBuilder("C");
		MSTCountry country = masterCountryService.findByCountryId(model.getMasterCountryModel().getId());
		referalCodeC.append(country.getShortName());
		CustomerCompany company = customerCompanyRepository.findFirstByOrderByIdDesc();

		LocalDateTime time = company.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		if (time.getYear() == LocalDateTime.now().getYear()) {
			referalCodeC.append(LocalDateTime.now().getYear() % 2000);
			String lastCount = company.getReferralCode().substring(company.getReferralCode().length() - 4);
			referalCodeC.append(String.format("%04d", Integer.parseInt(lastCount) + 1));
		} else {
			referalCodeC.append(LocalDateTime.now().getYear() % 2000);
			referalCodeC.append(String.format("%04d", 1));
		}
		return referalCodeC.toString();
	}

}

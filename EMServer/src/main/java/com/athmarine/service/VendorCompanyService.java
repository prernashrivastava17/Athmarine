package com.athmarine.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTCountry;
import com.athmarine.entity.MSTState;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.FinanceEmailAndPhoneNumberResponse;
import com.athmarine.request.MasterStateModel;
import com.athmarine.request.UserModel;
import com.athmarine.request.UserVendorModel;
import com.athmarine.request.UserVendorModelResponse;
import com.athmarine.request.VendorCompanyModel;
import com.athmarine.request.VendorCompanyResponse;
import com.athmarine.request.VendorCompanysModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.util.JwtTokenUtil;

@Service
public class VendorCompanyService {

	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private MasterStateService masterStateService;

	@Autowired
	private JwtTokenUtil accessToken;

	@Autowired
	private MasterCountryService masterCountryService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public OTPService otpService;

	public VendorCompanyModel createVendorComapny(UserVendorModel userVendorModel) {

		UserModel userModel = userVendorModel.getUserModel();

		VendorCompanyModel vendorCompanyModel = userVendorModel.getVendorCompanyModel();

		if(vendorCompanyRepository.existsByEmail(userModel.getEmail()) || vendorCompanyRepository.existsByPrimaryPhone(userModel.getPrimaryPhone()))
		{
			throw new InvalidInputException(AppConstant.ErrorTypes.USER_EXIST_ERROR,AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
					AppConstant.ErrorMessages.CONTACT_NO_ALREADY_EXIST_MESSAGE);
		}

		MSTState state = null;

		if (vendorCompanyModel.getMstateStateModel() != null) {

			if (vendorCompanyModel.getMstateStateModel().getId().equals(0)) {
				state = masterStateService.createState(vendorCompanyModel);
			} else {
				state = masterStateService.findState(vendorCompanyModel.getMstateStateModel().getId());
			}

		}
		String email = userModel.getEmail();
		if (userModel.getType() == null) {

		} else if (userModel.getType().equals("Individual")) {

			if (userModel.getPassword() != null && userModel.getType().equals("Individual")) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailOTP(email);

					}
				}).start();

			}
		}
		userModel = userDetailsService.createCompany(userModel);
		vendorCompanyModel.setId(null);
		vendorCompanyModel.setUserId(userModel.getId());
		vendorCompanyModel.setRegistrationStatus(RegistrationStatus.Address);
		vendorCompanyModel.setEmail(userModel.getEmail());
		vendorCompanyModel.setPrimaryPhone(userModel.getPrimaryPhone());

		return createVendor(vendorCompanyModel, state);

	}

	public List<FinanceEmailAndPhoneNumberResponse> getAllFinaneEmailAndPhoneNumberByCompanyId(Integer id) {
		VendorCompany company = findByIds(id);
		List<User> finanaceList = userRepository.getAllFinaneEmailAndPhoneNumberByCompanyId(company.getId());

		return finanaceList.stream()
				.map(finanace -> userDetailsService.convertToResponseFinanceEmailAndPhoneNumber(finanace))
				.collect(Collectors.toList());
	}

	public VendorCompanyModel createVendor(VendorCompanyModel vendorCompanyModel, MSTState state) {
		if (!checkAlreadyExists(vendorCompanyModel.getUserId())) {
			VendorCompany entity = convertToEntity(vendorCompanyModel, state);
			entity.setReferralUseCount(0);
			entity.setCity(vendorCompanyModel.getMasterCity());
			entity = vendorCompanyRepository.save(entity);

			entity.setReferralCode(generateReferralCode(entity.getId()));

			VendorCompanyModel vendorCompanyModels = convertToModel(vendorCompanyRepository.save(entity));

			VendorCompany findId = findByIds(vendorCompanyModels.getId());
			findId.setId(vendorCompanyModels.getId());
			findId.setCompanyUid(generateCompanyUID(findId.getId()));
			vendorCompanyRepository.save(entity);

			return vendorCompanyModels;

		} else {
			VendorCompany vendorCompany = findByIds(vendorCompanyModel.getUserId());
			vendorCompanyModel.setId(vendorCompany.getId());
			vendorCompanyModel.setCompanyUid(vendorCompany.getCompanyUid());
			vendorCompanyModel.setRegistrationStatus(vendorCompany.getRegistrationStatus());
			return updatedVendorCompany(vendorCompanyModel);
		}
//			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.VENDOR_COMPANY_EXIST_ERROR,
//					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
//					AppConstant.ErrorMessages.VENDOR_COMPANY_EXIST);

	}

	public VendorCompanyModel updatedVendorCompany(VendorCompanyModel vendorCompany) {
		MSTState state = null;

		if (vendorCompany.getMstateStateModel() != null) {

			state = masterStateService.findByIds(vendorCompany.getMstateStateModel().getId());

		}
		vendorCompany.setId(vendorCompany.getUserId());
		vendorCompany.setCompanyUid(vendorCompany.getCompanyUid());
		return convertToModel(vendorCompanyRepository.save(convertToEntity(vendorCompany, state)));

	}

	public VendorCompanyModel getVendorCompanyData(int id) {
		return convertToModel(vendorCompanyRepository.findById(id).orElseThrow(
				() -> new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE)));
	}

	public VendorCompanysModel getVendorCompanysData(Integer id) {
		return convertToModels(vendorCompanyRepository.findById(id).orElseThrow(
				() -> new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE)));
	}

	public UserVendorModel getVendorCompany(int id) {
		UserVendorModel model = new UserVendorModel();
		model.setVendorCompanyModel(getVendorCompanyData(id));
		model.setUserModel(userDetailsService.findById(id));
		return model;
	}

	public UserVendorModelResponse getVendorCompanyResponse(Integer id) {
		UserVendorModelResponse model = new UserVendorModelResponse();
		// model.setVendorCompanyModel(getVendorCompanyData(id));
		model.setUserModel(userDetailsService.findById(id));
		model.setVendorCompanysModel(getVendorCompanysData(id));
		return model;
	}

	public VendorCompanyModel findById(@NotBlank Integer id) {
		if (id == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(vendorCompanyRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE)));
		}
	}

	public VendorCompany findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);

		}
		return vendorCompanyRepository.findById(id).orElseThrow(
				() -> new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE));
	}

	public Integer getAllVendorCompanyCount() {

		return vendorCompanyRepository.getAllVendorCompanyCount();

	}

	public List<VendorCompanyResponse> getCompanyDataByCountryId(int id) {
		MSTCountry mstCountry = masterCountryService.findByCountryId(id);
		return mstCountry.getVendorCompany().stream().map(country -> converToResposeModel(country))
				.collect(Collectors.toList());
	}

	public boolean checkAlreadyExists(int id) {
		return vendorCompanyRepository.existsById(id);

	}

	public VendorCompanyModel deleteVendorCompany(Integer id) {
		VendorCompanyModel vendorCompany = findById(id);
		MSTState state = null;

		if (vendorCompany.getMstateStateModel() != null) {

			state = masterStateService.findByIds(vendorCompany.getMstateStateModel().getId());
		}
		vendorCompany.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(vendorCompanyRepository.save(convertToEntity(vendorCompany, state)));
	}

	public UserModel updateVendorCompany(UserVendorModel model) {

		VendorCompanyModel updateVendorCompany = model.getVendorCompanyModel();
		UserModel updateUserModel = model.getUserModel();
		VendorCompanyModel vendorCompany = findById(updateVendorCompany.getId());
		MSTState state = null;

		if (vendorCompany.getMstateStateModel() != null) {

			state = masterStateService.findByIds(vendorCompany.getMstateStateModel().getId());
		}

		UserModel userModel = userDetailsService.findById(updateUserModel.getId());

		updateVendorCompany.setId(vendorCompany.getId());

		updateUserModel.setId(userModel.getId());
		updateUserModel.setUid(userModel.getUid());
		vendorCompanyRepository.save(convertToEntity(updateVendorCompany, state));

		return userDetailsService.updateUserData(updateUserModel);

	}

	public Integer getAllVendorCompanyByUserReferarralCode(String referalCode) {

		return vendorCompanyRepository.getAllVendorCompanyByUserReferarralCode(referalCode);

	}

	public VendorCompanyModel updateVendorCompany(VendorCompanyModel model) {

		VendorCompanyModel vendorCompany = findById(model.getId());

		MSTState state = null;

		if (model.getMstateStateModel() != null) {

			state = masterStateService.findByIds(model.getMstateStateModel().getId());

		}

		model.setId(vendorCompany.getId());
		model.setAddress(vendorCompany.getAddress());
		model.setFaxno(vendorCompany.getFaxno());
		model.setPincode(vendorCompany.getPincode());
		model.setStatus(vendorCompany.getStatus());
		model.setReferralCode(vendorCompany.getReferralCode());
		model.setReferralCodeUsed(vendorCompany.getReferralCodeUsed());
		model.setCompanyStablished(model.getCompanyStablished());
		model.setYesOrNo(model.getYesOrNo());
		model.setCompanyUid(vendorCompany.getCompanyUid());
		model.setIsEstablishedLastFiveYear(model.getIsEstablishedLastFiveYear());
		model.setMasterCity(vendorCompany.getMasterCity());
		model.setRegisteredSuccessfully(vendorCompany.isRegisteredSuccessfully());
		model.setRegistrationStatus(RegistrationStatus.Business_Information);
		model.setEmail(vendorCompany.getEmail());
		model.setPrimaryPhone(vendorCompany.getPrimaryPhone());
		return convertToModel(vendorCompanyRepository.save(convertToEntity(model, state)));

	}

	public String generateReferralCode(Integer id) {

		StringBuilder referralCode = new StringBuilder("P");
		VendorCompany vendorCompany = findByIds(id);
		referralCode.append(vendorCompany.getCountryId().getShortName());
		referralCode.append(LocalDateTime.now().getYear() % 2000);
		referralCode.append(String.format("%04d", id));

		return referralCode.toString();
	}

	public String generateCompanyUID(Integer model) {

		VendorCompany company = findByIds(model);

		StringBuilder referralCode = new StringBuilder("P");
		// VendorCompany vendorCompany = findByIds(id);
		MSTCountry country = masterCountryService.findByCountryId(company.getCountryId().getId());
		referralCode.append(country.getShortName());
		// VendorCompany company = vendorCompanyRepository.findFirstByOrderByIdDesc();

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

	VendorCompany convertToEntity(VendorCompanyModel model, MSTState state) {

		return VendorCompany.builder().id(model.getId()).user(userDetailsService.findUserEntityById(model.getUserId()))
				.logo(model.getLogo()).address(model.getAddress()).city(model.getMasterCity())
				.pincode(model.getPincode()).city_of_registration(model.getCity_of_registration())
				.isSameAsAdmin(model.isSameAsAdmin()).isRegistered(model.isRegistered())
				.country_of_registration(model.getCountry_of_registration()).faxno(model.getFaxno())
				.registrationNo(model.getRegistrationNo()).yearOfReg(model.getYearOfReg()).status(model.getStatus())
				.stateId(state).countryId(masterCountryService.findByCountryId(model.getMasterCountryModel().getId()))
				.registrationStatus(model.getRegistrationStatus()).referralCodeUsed(model.getReferralCodeUsed())
				.referralCode(model.getReferralCode()).yesOrNo(model.getYesOrNo())
				.companyStablished(model.getCompanyStablished()).CompanyUid(model.getCompanyUid())
				.isEstablishedLastFiveYear(model.getIsEstablishedLastFiveYear())
				.isRegisteredSuccessfully(model.isRegisteredSuccessfully()).status(model.getStatus()).stateId(state)
				.countryId(masterCountryService.findByCountryId(model.getMasterCountryModel().getId()))
				.email(model.getEmail()).primaryPhone(model.getPrimaryPhone()).build();

	}

	VendorCompanyModel convertToModel(VendorCompany entity) {

		UserModel userModel = userDetailsService.findById(entity.getUser().getId());

		// MasterCountryModel masterCountryModel =
		// masterCountryService.findByIds(entity.getCountryId().getId());
		MasterStateModel masterState = null;
		Integer masterStateModel = (entity.getStateId() == null) ? 0 : entity.getStateId().getId();
		if (masterStateModel == 0) {
			masterState = null;
		} else {
			masterState = masterStateService.findById(masterStateModel);
		}

		// String uid = userModel.getUid();

		return VendorCompanyModel.builder().id(entity.getId()).address(entity.getAddress())
				.city_of_registration(entity.getCity_of_registration())
				.country_of_registration(entity.getCountry_of_registration()).faxno(entity.getFaxno())
				.logo(entity.getLogo()).masterCity(entity.getCity()).mstateStateModel(masterState)
				.masterCountryModel(masterCountryService.findByIds(entity.getCountryId().getId()))
				.pincode(entity.getPincode()).registered(entity.isRegistered())
				.registrationNo(entity.getRegistrationNo()).sameAsAdmin(entity.isSameAsAdmin())
				.status(entity.getStatus()).yearOfReg(entity.getYearOfReg())
				.token(accessToken.generateToken(userModel.getEmail())).companyUid(entity.getCompanyUid())
				.registrationStatus(entity.getRegistrationStatus()).referralCode(entity.getReferralCode())
				.referralCodeUsed(entity.getReferralCodeUsed()).yesOrNo(entity.getYesOrNo())
				.companyStablished(entity.getCompanyStablished())
				.isEstablishedLastFiveYear(entity.getIsEstablishedLastFiveYear())
				.isRegisteredSuccessfully(entity.isRegisteredSuccessfully())
				.email(entity.getEmail()).primaryPhone(entity.getPrimaryPhone()).build();
	}

	VendorCompanysModel convertToModels(VendorCompany entity) {

		UserModel userModel = userDetailsService.findById(entity.getUser().getId());

		MasterStateModel masterStateModel = null;

		if (entity.getStateId() == null) {

			masterStateModel = null;

		} else {
			masterStateModel = masterStateService.findById(entity.getStateId().getId());
			masterStateModel.setId(masterStateModel.getId());
			masterStateModel.setName(masterStateModel.getName());
			masterStateModel.setStatus(masterStateModel.getStatus());
			// masterStateModel=null;
		}
		// String uid = userModel.getUid();

		return VendorCompanysModel.builder().id(entity.getId()).address(entity.getAddress())
				.city_of_registration(entity.getCity_of_registration())
				.country_of_registration(entity.getCountry_of_registration()).faxno(entity.getFaxno())
				.logo(entity.getLogo()).masterCityModel(entity.getCity()).mstateStateModel(masterStateModel)
				.masterCountryModel(masterCountryService.findByIds(entity.getCountryId().getId()))
				.pincode(entity.getPincode()).registered(entity.isRegistered())
				.registrationNo(entity.getRegistrationNo()).sameAsAdmin(entity.isSameAsAdmin())
				.status(entity.getStatus()).yearOfReg(entity.getYearOfReg())
				.token(accessToken.generateToken(userModel.getEmail())).companyUid(entity.getCompanyUid())
				.registrationStatus(entity.getRegistrationStatus()).referralCode(entity.getReferralCode())
				.referralCodeUsed(entity.getReferralCodeUsed()).yesOrNo(entity.getYesOrNo())
				.isEstablishedLastFiveYear(entity.getIsEstablishedLastFiveYear())
				.companyStablished(entity.getCompanyStablished())
				.isRegisteredSuccessfully(entity.isRegisteredSuccessfully()).build();
	}

	public VendorCompanyResponse converToResposeModel(VendorCompany entity) {

		UserModel userModel = userDetailsService.findById(entity.getId());

		return VendorCompanyResponse.builder().id(entity.getId()).logo(entity.getLogo())
				.companyName(userModel.getName()).build();

	}

}
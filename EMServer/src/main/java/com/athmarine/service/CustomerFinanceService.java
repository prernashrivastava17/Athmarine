package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CustomerCompany;
import com.athmarine.entity.Finance;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.Turnover;
import com.athmarine.exception.CustomerCompanyNotFoundException;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.CustomerCompanyRepository;
import com.athmarine.repository.FinanceRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.request.FinanceModel;
import com.athmarine.request.RoleModels;
import com.athmarine.request.TurnoverModel;
import com.athmarine.request.UserFinanceTurnoverModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class CustomerFinanceService {

	@Autowired
	FinanceRepository financeRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MasterCountryService masterCountryService;

	@Autowired
	TurnoverService turnoverService;

	@Autowired
	public OTPService otpService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerCompanyService customerCompanyService;

	@Autowired
	CustomerCompanyRepository customerCompanyRepository;

	public List<FinanceModel> saveCustomerFinance(List<UserFinanceTurnoverModel> model) {

		for (int i = 0; i < model.size(); i++) {
			UserModel FiananceUserModel = model.get(i).getUserModel();

			if (!FiananceUserModel.getSameASUser().equals("Yes")) {

				if (userRepository.existsByEmail(FiananceUserModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
							FiananceUserModel.getEmail(), AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}
				if (userRepository.existsByPrimaryPhone(FiananceUserModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							FiananceUserModel.getPrimaryPhone(),
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}
		}
		List<FinanceModel> resposeCustomerFinance = new ArrayList<FinanceModel>();
		for (Integer i = 0; i < model.size(); i++) {
			UserModel userModel = model.get(i).getUserModel();

			if (!userModel.getSameASUser().equals("Yes")) {

				FinanceModel financeModel = model.get(i).getFinanceModel();

				List<TurnoverModel> turnoverModels = model.get(i).getTurnoverModels();
				if (userRepository.existsByEmail(userModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
							AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}

				if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}

				String userEmail = userModel.getEmail();

				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailOTP(userEmail);
					}
				}).start();
				userModel = userDetailsService.createUser(userModel);
				financeModel.setId(null);
				financeModel.setUserId(userModel.getId());
				financeModel.setTurnover(turnoverModels);
				resposeCustomerFinance.add(createCustomerFinance(financeModel));
				CustomerCompany customerCompany = customerCompanyService
						.findByIds(model.get(i).getUserModel().getCompanyId());
				customerCompany.setRegistrationStatus(RegistrationStatus.Finance);
				customerCompanyRepository.save(customerCompany);
				userModel.setUid(userDetailsService.generateUniqueIdForCustomer(userModel.getId(), 0 + i + 1));
				userDetailsService.updateUserData(userModel);
			} else if (userModel.getSameASUser().equals("Yes")) {
				UserModel bidderModel = userModel;
				userDetailsService.updateSameUser(bidderModel);
			}
		}

		return resposeCustomerFinance;
	}

	public FinanceModel createCustomerFinance(FinanceModel financeModel) {
		if (financeModel == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE);
		}

		if (!checkAlreadyExists(financeModel.getUserId())) {
			return convertToModel(financeRepository.save(convertToEntity(financeModel)));

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);

	}

	public FinanceModel getFinanceDetails(int id) {
		return convertToModel(financeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
	}

	public FinanceModel updatesCustomerFinance(UserFinanceTurnoverModel model) {

		UserModel userModel = userDetailsService.findUserEntityByIds(model.getUserModel().getId());
		userModel.setId(model.getUserModel().getId());
		FinanceModel financeModel = findById(model.getFinanceModel().getId());

		List<TurnoverModel> turnoverModels = model.getTurnoverModels();
		userModel = userDetailsService.updateUserData(userModel);
		financeModel.setId(financeModel.getId());
		financeModel.setTurnover(turnoverModels);
		return updateCustomerFinance(financeModel);
	}

	public FinanceModel updateCustomerFinance(FinanceModel financeModel) {

		FinanceModel finanace = findById(financeModel.getId());

		financeModel.setId(finanace.getId());

		return convertToModel(financeRepository.save(convertToEntity(financeModel)));

	}

	public List<UserModel> getAllFinanceByCompanyId(int id) {
		List<UserModel> userList = userDetailsService.getAllCustomerUserByCompanyId(id);
		List<UserModel> response = new ArrayList<UserModel>();
		for (int i = 0; i < userList.size(); i++) {
			for (RoleModels role : userList.get(i).getRoleModel()) {
				if (role.getName().equals(AppConstant.RoleValues.ROLE_FINANCE)) {

					response.add(userList.get(i));
				}
			}
		}
		return response;
	}

	public List<UserFinanceTurnoverModel> getAllFinanceByCompnayId(int id) {
		if (customerCompanyService.checkAlreadyExists(id)) {
			List<UserFinanceTurnoverModel> financeModels = new ArrayList<UserFinanceTurnoverModel>();
			List<UserModel> userList = getAllFinanceByCompanyId(id);
			for (int i = 0; i < userList.size(); i++) {
				Finance finance = findByIds(userList.get(i).getId());
				financeModels.add(convertToResponseModel(finance));
			}
			return financeModels;

		} else {
			throw new CustomerCompanyNotFoundException(AppConstant.ErrorTypes.CUSTOMER_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.CUSTOMER_COMPANY_ERROR_CODE,
					AppConstant.ErrorMessages.CUSTOMER_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public boolean checkAlreadyExists(int id) {
		return financeRepository.existsById(id);

	}

	public FinanceModel deleteFinanceDetails(Integer id) {

		FinanceModel finance = findById(id);

		finance.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(financeRepository.save(convertToEntity(finance)));

	}

	private Finance findByIds(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return financeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
		}
	}

	private FinanceModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return convertToModel(financeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	private Finance convertToEntity(FinanceModel model) {

		List<Turnover> turnoverList = model.getTurnover().stream()
				.map(turnover -> turnoverService.convertToEntity(turnover)).collect(Collectors.toList());

		return Finance.builder().id(model.getId()).user(userDetailsService.findUserEntityById(model.getUserId()))
				.turnover(turnoverList).currency(model.getCurrency()).status(model.getStatus()).type("customer")
				.build();

	}

	private UserFinanceTurnoverModel convertToResponseModel(Finance entity) {
		List<TurnoverModel> turnoverList = entity.getTurnover().stream()
				.map(turnover -> turnoverService.convertToModel(turnover)).collect(Collectors.toList());
		FinanceModel financeModel = new FinanceModel();
		financeModel.setCurrency(entity.getCurrency());
		financeModel.setId(entity.getId());
		financeModel.setStatus(entity.getStatus());

		return UserFinanceTurnoverModel.builder().id(entity.getId()).financeModel(financeModel)
				.userModel(userDetailsService.convertToModel(entity.getUser())).turnoverModels(turnoverList).build();

	}

	private FinanceModel convertToModel(Finance entity) {

		List<TurnoverModel> turnoverList = entity.getTurnover().stream()
				.map(turnover -> turnoverService.convertToModel(turnover)).collect(Collectors.toList());

		return FinanceModel.builder().id(entity.getId()).userId(entity.getId()).turnover(turnoverList)
				.currency(entity.getCurrency()).status(entity.getStatus()).build();

	}

}
